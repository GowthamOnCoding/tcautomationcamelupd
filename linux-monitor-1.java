package com.monitor.server;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

@Component
public class ServerMonitorRoutes extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        // Configure REST endpoints
        restConfiguration()
            .component("servlet")
            .bindingMode(RestBindingMode.json)
            .dataFormatProperty("prettyPrint", "true")
            .contextPath("/api");

        // REST endpoints
        rest("/monitor")
            .get("/system").to("direct:getSystemInfo")
            .get("/processes").to("direct:getProcesses")
            .get("/top-cpu").to("direct:getTopCPU")
            .get("/top-memory").to("direct:getTopMemory")
            .post("/kill/{pid}").to("direct:killProcess")
            .post("/command").to("direct:runCommand");

        // Route implementations
        from("direct:getSystemInfo")
            .process(exchange -> {
                Map<String, Object> systemInfo = new HashMap<>();
                
                // CPU Usage
                Process cpuProcess = Runtime.getRuntime().exec("top -bn1 | grep \"Cpu(s)\"");
                BufferedReader cpuReader = new BufferedReader(new InputStreamReader(cpuProcess.getInputStream()));
                String cpuLine = cpuReader.readLine();
                systemInfo.put("cpuUsage", parseCpuUsage(cpuLine));

                // Memory Usage
                Process memProcess = Runtime.getRuntime().exec("free -m");
                BufferedReader memReader = new BufferedReader(new InputStreamReader(memProcess.getInputStream()));
                String memLine = memReader.readLine();
                systemInfo.put("memoryInfo", parseMemoryInfo(memLine));

                // Disk Usage
                Process diskProcess = Runtime.getRuntime().exec("df -h");
                BufferedReader diskReader = new BufferedReader(new InputStreamReader(diskProcess.getInputStream()));
                systemInfo.put("diskInfo", parseDiskInfo(diskReader));

                exchange.getMessage().setBody(systemInfo);
            });

        from("direct:getProcesses")
            .process(exchange -> {
                Process process = Runtime.getRuntime().exec("ps aux");
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                exchange.getMessage().setBody(parseProcessList(reader));
            });

        from("direct:getTopCPU")
            .process(exchange -> {
                Process process = Runtime.getRuntime().exec("ps aux --sort=-%cpu | head -n 11");
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                exchange.getMessage().setBody(parseProcessList(reader));
            });

        from("direct:getTopMemory")
            .process(exchange -> {
                Process process = Runtime.getRuntime().exec("ps aux --sort=-%mem | head -n 11");
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                exchange.getMessage().setBody(parseProcessList(reader));
            });

        from("direct:killProcess")
            .process(exchange -> {
                String pid = exchange.getMessage().getHeader("pid", String.class);
                Process process = Runtime.getRuntime().exec("kill " + pid);
                exchange.getMessage().setBody(Collections.singletonMap("status", "Process " + pid + " terminated"));
            });

        from("direct:runCommand")
            .process(exchange -> {
                String command = exchange.getMessage().getBody(String.class);
                Process process = Runtime.getRuntime().exec(command);
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                exchange.getMessage().setBody(parseCommandOutput(reader));
            });
    }

    private double parseCpuUsage(String cpuLine) {
        // Parse CPU usage from top command output
        if (cpuLine == null) return 0.0;
        String[] parts = cpuLine.split(",");
        if (parts.length > 0) {
            String userCpu = parts[0].replaceAll("[^0-9.]", "");
            return Double.parseDouble(userCpu);
        }
        return 0.0;
    }

    private Map<String, Object> parseMemoryInfo(String memLine) {
        // Parse memory information from free command
        Map<String, Object> memInfo = new HashMap<>();
        if (memLine == null) return memInfo;
        
        String[] parts = memLine.split("\\s+");
        if (parts.length >= 3) {
            memInfo.put("total", Long.parseLong(parts[1]));
            memInfo.put("used", Long.parseLong(parts[2]));
            memInfo.put("free", Long.parseLong(parts[3]));
        }
        return memInfo;
    }

    private List<Map<String, String>> parseDiskInfo(BufferedReader reader) throws Exception {
        List<Map<String, String>> diskInfo = new ArrayList<>();
        String line;
        boolean skipHeader = true;
        
        while ((line = reader.readLine()) != null) {
            if (skipHeader) {
                skipHeader = false;
                continue;
            }
            
            String[] parts = line.split("\\s+");
            if (parts.length >= 6) {
                Map<String, String> partition = new HashMap<>();
                partition.put("filesystem", parts[0]);
                partition.put("size", parts[1]);
                partition.put("used", parts[2]);
                partition.put("available", parts[3]);
                partition.put("usePercentage", parts[4]);
                partition.put("mountPoint", parts[5]);
                diskInfo.add(partition);
            }
        }
        return diskInfo;
    }

    private List<Map<String, String>> parseProcessList(BufferedReader reader) throws Exception {
        List<Map<String, String>> processes = new ArrayList<>();
        String line;
        boolean skipHeader = true;
        
        while ((line = reader.readLine()) != null) {
            if (skipHeader) {
                skipHeader = false;
                continue;
            }
            
            String[] parts = line.split("\\s+");
            if (parts.length >= 11) {
                Map<String, String> process = new HashMap<>();
                process.put("user", parts[0]);
                process.put("pid", parts[1]);
                process.put("cpu", parts[2]);
                process.put("memory", parts[3]);
                process.put("command", String.join(" ", Arrays.copyOfRange(parts, 10, parts.length)));
                processes.add(process);
            }
        }
        return processes;
    }

    private Map<String, String> parseCommandOutput(BufferedReader reader) throws Exception {
        StringBuilder output = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }
        return Collections.singletonMap("output", output.toString());
    }
}
