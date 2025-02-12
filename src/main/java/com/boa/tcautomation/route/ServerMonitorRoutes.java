package com.boa.tcautomation.route;

import com.boa.tcautomation.model.HostDetails;
import com.boa.tcautomation.repository.HostDetailsRepository;
import com.boa.tcautomation.util.SshUtil;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class ServerMonitorRoutes extends RouteBuilder {

    @Autowired
    private HostDetailsRepository hostDetailsRepository;

    @Autowired
    private SshUtil sshService;

    @Override
    public void configure() throws Exception {
        restConfiguration()
                .component("servlet")
                .bindingMode(RestBindingMode.json)
                .dataFormatProperty("prettyPrint", "true")
                .contextPath("/api");

        // Host endpoints
        rest("/hosts")
                .get().to("direct:getHosts")
                .get("/{hostName}").to("direct:getHost")
                .get("/category/{category}").to("direct:getHostsByCategory");

        // Monitoring endpoints
        rest("/monitor")
                .get("/system/{hostName}").to("direct:getSystemInfo")
                .get("/processes/{hostName}").to("direct:getProcesses")
                .get("/top-cpu/{hostName}").to("direct:getTopCPU")
                .get("/top-memory/{hostName}").to("direct:getTopMemory")
                .post("/kill/{hostName}/{pid}").to("direct:killProcess");

        // Host routes
        from("direct:getHosts")
                .process(exchange -> {
                    List<HostDetails> hosts = hostDetailsRepository.findByIsActiveTrue();
                    exchange.getMessage().setBody(hosts);
                });

        from("direct:getHost")
                .process(exchange -> {
                    String hostName = exchange.getMessage().getHeader("hostName", String.class);
                    HostDetails host = hostDetailsRepository.findById(hostName).orElseThrow();
                    exchange.getMessage().setBody(host);
                });

        from("direct:getHostsByCategory")
                .process(exchange -> {
                    String category = exchange.getMessage().getHeader("category", String.class);
                    List<HostDetails> hosts = hostDetailsRepository.findAll().stream()
                            .filter(host -> category.equals(host.getCategory()))
                            .toList();
                    exchange.getMessage().setBody(hosts);
                });

        // Monitoring routes
        from("direct:getSystemInfo")
                .process(exchange -> {
                    String hostName = exchange.getMessage().getHeader("hostName", String.class);
                    HostDetails host = hostDetailsRepository.findById(hostName).orElseThrow();

                    Map<String, Object> systemInfo = new HashMap<>();

                    // CPU Info
                    String cpuInfo = sshService.executeCommand(
                            host.getHostName(),
                            "top -bn1 | grep 'Cpu(s)'"
                    );
                    systemInfo.put("cpu", parseCpuInfo(cpuInfo));

                    // Memory Info
                    String memInfo = sshService.executeCommand(
                            host.getHostName(),
                            "free -m"
                    );
                    systemInfo.put("memory", parseMemoryInfo(memInfo));

                    // Disk Info
                    String diskInfo = sshService.executeCommand(
                            host.getHostName(),
                            "df -h"
                    );
                    systemInfo.put("disk", parseDiskInfo(diskInfo));

                    // Update last checked time
                    host.setLastUpdated(new Date());
                    hostDetailsRepository.save(host);

                    exchange.getMessage().setBody(systemInfo);
                });

        from("direct:getProcesses")
                .process(exchange -> {
                    String hostName = exchange.getMessage().getHeader("hostName", String.class);
                    HostDetails host = hostDetailsRepository.findById(hostName).orElseThrow();

                    String output = sshService.executeCommand(
                            host.getHostName(),
                            "ps aux"
                    );

                    List<Map<String, Object>> processes = parseProcessList(output);
                    // Filter processes based on host's process pattern if specified
                    if (host.getProcess() != null && !host.getProcess().isEmpty()) {
                        processes = processes.stream()
                                .filter(p -> ((String)p.get("command")).contains(host.getProcess()))
                                .toList();
                    }

                    exchange.getMessage().setBody(processes);
                });

        from("direct:getTopCPU")
                .process(exchange -> {
                    String hostName = exchange.getMessage().getHeader("hostName", String.class);
                    HostDetails host = hostDetailsRepository.findById(hostName).orElseThrow();

                    String output = sshService.executeCommand(
                            host.getHostName(),
                            "ps aux --sort=-%cpu | head -11"
                    );

                    exchange.getMessage().setBody(parseProcessList(output));
                });

        from("direct:getTopMemory")
                .process(exchange -> {
                    String hostName = exchange.getMessage().getHeader("hostName", String.class);
                    HostDetails host = hostDetailsRepository.findById(hostName).orElseThrow();

                    String output = sshService.executeCommand(
                            host.getHostName(),
                            "ps aux --sort=-%mem | head -11"
                    );

                    exchange.getMessage().setBody(parseProcessList(output));
                });

        from("direct:killProcess")
                .process(exchange -> {
                    String hostName = exchange.getMessage().getHeader("hostName", String.class);
                    String pid = exchange.getMessage().getHeader("pid", String.class);
                    HostDetails host = hostDetailsRepository.findById(hostName).orElseThrow();

                    sshService.executeCommand(
                            host.getHostName(),
                            "kill " + pid
                    );

                    exchange.getMessage().setBody(Map.of("status", "success", "message", "Process killed"));
                });
    }

    // Helper methods for parsing command outputs (same as before)
    private Map<String, Object> parseCpuInfo(String cpuInfo) {
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
                    Map<String, Object> process = getProcessMap(parts);
                    result.add(process);
                }
            }
        }
        return result;
    }

    private static Map<String, Object> getProcessMap(String[] parts) {
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
        return process;
    }
}
