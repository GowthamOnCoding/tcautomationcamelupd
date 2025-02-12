// pom.xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.monitor</groupId>
    <artifactId>server-monitor</artifactId>
    <version>1.0.0</version>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.7.0</version>
    </parent>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>org.apache.camel.springboot</groupId>
            <artifactId>camel-spring-boot-starter</artifactId>
            <version>3.18.0</version>
        </dependency>
        <dependency>
            <groupId>org.apache.camel</groupId>
            <artifactId>camel-servlet-starter</artifactId>
            <version>3.18.0</version>
        </dependency>
        <dependency>
            <groupId>com.jcraft</groupId>
            <artifactId>jsch</artifactId>
            <version>0.1.55</version>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.28</version>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>

// application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/server_monitor
spring.datasource.username=root
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

server.port=8080
camel.servlet.mapping.context-path=/api/*

// ServerMonitorApplication.java
package com.monitor.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ServerMonitorApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServerMonitorApplication.class, args);
    }
}

// model/Server.java
package com.monitor.server.model;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "servers")
public class Server {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String hostname;
    private String description;
    private String username;
    
    @Column(name = "ssh_key")
    private String sshKey;
    
    private boolean active;
}

// repository/ServerRepository.java
package com.monitor.server.repository;

import com.monitor.server.model.Server;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ServerRepository extends JpaRepository<Server, Long> {
    List<Server> findByActiveTrue();
}

// service/SSHService.java
package com.monitor.server.service;

import com.jcraft.jsch.*;
import org.springframework.stereotype.Service;
import java.io.*;

@Service
public class SSHService {
    public String executeCommand(String hostname, String username, String sshKey, String command) {
        JSch jsch = new JSch();
        Session session = null;
        ChannelExec channel = null;
        
        try {
            // Add private key
            jsch.addIdentity("server_key", sshKey.getBytes(), null, null);
            
            session = jsch.getSession(username, hostname, 22);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            
            channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(command);
            
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            channel.setOutputStream(outputStream);
            channel.connect();
            
            while (channel.isConnected()) {
                Thread.sleep(100);
            }
            
            return outputStream.toString();
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to execute command: " + e.getMessage(), e);
        } finally {
            if (channel != null) {
                channel.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        }
    }
}

// routes/ServerMonitorRoutes.java
package com.monitor.server.routes;

import com.monitor.server.model.Server;
import com.monitor.server.repository.ServerRepository;
import com.monitor.server.service.SSHService;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class ServerMonitorRoutes extends RouteBuilder {

    @Autowired
    private ServerRepository serverRepository;
    
    @Autowired
    private SSHService sshService;

    @Override
    public void configure() throws Exception {
        restConfiguration()
            .component("servlet")
            .bindingMode(RestBindingMode.json)
            .dataFormatProperty("prettyPrint", "true")
            .contextPath("/api");

        // Server endpoints
        rest("/servers")
            .get().to("direct:getServers")
            .get("/{id}").to("direct:getServer");

        // Monitoring endpoints
        rest("/monitor")
            .get("/system/{serverId}").to("direct:getSystemInfo")
            .get("/processes/{serverId}").to("direct:getProcesses")
            .get("/top-cpu/{serverId}").to("direct:getTopCPU")
            .get("/top-memory/{serverId}").to("direct:getTopMemory")
            .post("/kill/{serverId}/{pid}").to("direct:killProcess")
            .post("/command/{serverId}").to("direct:runCommand");

        // Server routes
        from("direct:getServers")
            .process(exchange -> {
                List<Server> servers = serverRepository.findByActiveTrue();
                exchange.getMessage().setBody(servers);
            });

        from("direct:getServer")
            .process(exchange -> {
                Long id = exchange.getMessage().getHeader("id", Long.class);
                Server server = serverRepository.findById(id).orElseThrow();
                exchange.getMessage().setBody(server);
            });

        // Monitoring routes
        from("direct:getSystemInfo")
            .process(exchange -> {
                Long serverId = exchange.getMessage().getHeader("serverId", Long.class);
                Server server = serverRepository.findById(serverId).orElseThrow();
                
                Map<String, Object> systemInfo = new HashMap<>();
                
                // CPU Info
                String cpuInfo = sshService.executeCommand(
                    server.getHostname(),
                    server.getUsername(),
                    server.getSshKey(),
                    "top -bn1 | grep 'Cpu(s)'"
                );
                systemInfo.put("cpu", parseCpuInfo(cpuInfo));
                
                // Memory Info
                String memInfo = sshService.executeCommand(
                    server.getHostname(),
                    server.getUsername(),
                    server.getSshKey(),
                    "free -m"
                );
                systemInfo.put("memory", parseMemoryInfo(memInfo));
                
                // Disk Info
                String diskInfo = sshService.executeCommand(
                    server.getHostname(),
                    server.getUsername(),
                    server.getSshKey(),
                    "df -h"
                );
                systemInfo.put("disk", parseDiskInfo(diskInfo));
                
                exchange.getMessage().setBody(systemInfo);
            });

        from("direct:getProcesses")
            .process(exchange -> {
                Long serverId = exchange.getMessage().getHeader("serverId", Long.class);
                Server server = serverRepository.findById(serverId).orElseThrow();
                
                String output = sshService.executeCommand(
                    server.getHostname(),
                    server.getUsername(),
                    server.getSshKey(),
                    "ps aux"
                );
                
                exchange.getMessage().setBody(parseProcessList(output));
            });

        from("direct:getTopCPU")
            .process(exchange -> {
                Long serverId = exchange.getMessage().getHeader("serverId", Long.class);
                Server server = serverRepository.findById(serverId).orElseThrow();
                
                String output = sshService.executeCommand(
                    server.getHostname(),
                    server.getUsername(),
                    server.getSshKey(),
                    "ps aux --sort=-%cpu | head -11"
                );
                
                exchange.getMessage().setBody(parseProcessList(output));
            });

        from("direct:getTopMemory")
            .process(exchange -> {
                Long serverId = exchange.getMessage().getHeader("serverId", Long.class);
                Server server = serverRepository.findById(serverId).orElseThrow();
                
                String output = sshService.executeCommand(
                    server.getHostname(),
                    server.getUsername(),
                    server.getSshKey(),
                    "ps aux --sort=-%mem | head -11"
                );
                
                exchange.getMessage().setBody(parseProcessList(output));
            });

        from("direct:killProcess")
            .process(exchange -> {
                Long serverId = exchange.getMessage().getHeader("serverId", Long.class);
                String pid = exchange.getMessage().getHeader("pid", String.class);
                Server server = serverRepository.findById(serverId).orElseThrow();
                
                sshService.executeCommand(
                    server.getHostname(),
                    server.getUsername(),
                    server.getSshKey(),
                    "kill " + pid
                );
                
                exchange.getMessage().setBody(Map.of("status", "success", "message", "Process killed"));
            });

        from("direct:runCommand")
            .process(exchange -> {
                Long serverId = exchange.getMessage().getHeader("serverId", Long.class);
                String command = exchange.getMessage().getBody(String.class);
                Server server = serverRepository.findById(serverId).orElseThrow();
                
                String output = sshService.executeCommand(
                    server.getHostname(),
                    server.getUsername(),
                    server.getSshKey(),
                    command
                );
                
                exchange.getMessage().setBody(Map.of("output", output));
            });
    }

    private Map<String, Object> parseCpuInfo(String cpuInfo) {
        // Parse CPU usage information
        Map<String, Object> result = new HashMap<>();
        if (cpuInfo != null && !cpuInfo.isEmpty()) {
            String[] parts = cpuInfo.split(",");
            result.put("user", parsePercentage(parts[0]));
            result.put("system", parsePercentage(parts[2]));
            result.put("idle", parsePercentage(parts[3]));
        }
        return result;
    }

    private double parsePercentage(String text) {
        try {
            return Double.parseDouble(text.replaceAll("[^0-9.]", ""));
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private Map<String, Object> parseMemoryInfo(String memInfo) {
        Map<String, Object> result = new HashMap<>();
        if (memInfo != null && !memInfo.isEmpty()) {
            String[] lines = memInfo.split("\n");
            if (lines.length > 1) {
                String[] parts = lines[1].trim().split("\\s+");
                result.put("total", Long.parseLong(parts[1]));
                result.put("used", Long.parseLong(parts[2]));
                result.put("free", Long.parseLong(parts[3]));
            }
        }
        return result;
    }

    private List<Map<String, Object>> parseDiskInfo(String diskInfo) {
        List<Map<String, Object>> result = new ArrayList<>();
        if (diskInfo != null && !diskInfo.isEmpty()) {
            String[] lines = diskInfo.split("\n");
            for (int i = 1; i < lines.length; i++) {
                String[] parts = lines[i].trim().split("\\s+");
                if (parts.length >= 6) {
                    Map<String, Object> disk = new HashMap<>();
                    disk.put("filesystem", parts[0]);
                    disk.put("size", parts[1]);
                    disk.put("used", parts[2]);
                    disk.put("available", parts[3]);
                    disk.put("usePercentage", parts[4]);
                    disk.put("mountPoint", parts[5]);
                    result.add(disk);
                }
            }
        }
        return result;
    }

    private List<Map<String, Object>> parseProcessList(String processInfo) {
        List<Map<String, Object>> result = new ArrayList<>();
        if (processInfo != null && !processInfo.isEmpty()) {
            String[] lines = processInfo.split("\n");
            for (int i = 1; i < lines.length; i++) {
                String[] parts = lines[i].trim().split("\\s+", 11);
                if (parts.length >= 11) {
                    Map<String, Object> process = new HashMap<>();
                    process.put("user", parts[0]);
                    process.put("pid", parts[1]);
                    process.put("cpu", Double.parseDouble(parts[2]));
                    process.put("memory", Double.parseDouble(parts[3]));
                    process.put("vsz", parts[4]);
                    process.put("rss", parts[5]);
                    process.put("tty", parts[6]);
                    process.put("stat", parts[7]);
                    process.put("start", parts[8]);
                    process.put("time", parts[9]);
                    process.put("command", parts[10]);
                    result.add(process);
                }
            }
        }
        return result;
    }
}
