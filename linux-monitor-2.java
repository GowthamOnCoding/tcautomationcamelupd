package com.monitor.server;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

@Component
public class ServerMonitorRoutes extends RouteBuilder {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void configure() throws Exception {
        restConfiguration()
            .component("servlet")
            .bindingMode(RestBindingMode.json)
            .dataFormatProperty("prettyPrint", "true")
            .contextPath("/api");

        // Add new endpoint for server list
        rest("/servers")
            .get().to("direct:getServers");

        rest("/monitor")
            .get("/system/{serverId}").to("direct:getSystemInfo")
            .get("/processes/{serverId}").to("direct:getProcesses")
            .get("/top-cpu/{serverId}").to("direct:getTopCPU")
            .get("/top-memory/{serverId}").to("direct:getTopMemory")
            .post("/kill/{serverId}/{pid}").to("direct:killProcess")
            .post("/command/{serverId}").to("direct:runCommand");

        // New route for fetching servers
        from("direct:getServers")
            .process(exchange -> {
                List<Map<String, Object>> servers = jdbcTemplate.queryForList(
                    "SELECT id, name, hostname, description FROM servers WHERE active = true"
                );
                exchange.getMessage().setBody(servers);
            });

        // Modified routes to include server connection logic
        from("direct:getSystemInfo")
            .process(exchange -> {
                String serverId = exchange.getMessage().getHeader("serverId", String.class);
                Map<String, Object> serverInfo = getServerConnection(serverId);
                // Use serverInfo to connect to the specific server
                // Rest of the system info logic remains the same...
            });

        // Similar modifications for other routes...
    }

    private Map<String, Object> getServerConnection(String serverId) {
        return jdbcTemplate.queryForMap(
            "SELECT hostname, username, ssh_key FROM servers WHERE id = ?",
            serverId
        );
    }

    // Rest of the existing methods remain the same...
}

// Add new entity class for Server
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

    // Getters and setters...
}
