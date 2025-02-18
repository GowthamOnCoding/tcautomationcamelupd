package com.boa.tcautomation.route;

import com.boa.tcautomation.model.HostDetails;
import com.boa.tcautomation.repository.HostDetailsRepository;
import com.boa.tcautomation.service.MockMonitoringService;
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
    private MockMonitoringService mockService;

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

        // Host routes (unchanged)
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

        // Modified monitoring routes to use mock service
        from("direct:getSystemInfo")
                .process(exchange -> {
                    String hostName = exchange.getMessage().getHeader("hostName", String.class);
                    HostDetails host = hostDetailsRepository.findById(hostName).orElseThrow();
                    
                    Map<String, Object> systemInfo = mockService.getSystemInfo();
                    
                    // Update last checked time
                    host.setLastUpdated(new Date());
                    hostDetailsRepository.save(host);
                    
                    exchange.getMessage().setBody(systemInfo);
                });

        from("direct:getProcesses")
                .process(exchange -> {
                    String hostName = exchange.getMessage().getHeader("hostName", String.class);
                    HostDetails host = hostDetailsRepository.findById(hostName).orElseThrow();
                    
                    List<Map<String, Object>> processes = mockService.getProcesses();
                    if (host.getProcess() != null && !host.getProcess().isEmpty()) {
                        processes = processes.stream()
                                .filter(p -> ((String)p.get("command")).contains(host.getProcess()))
                                .toList();
                    }
                    
                    exchange.getMessage().setBody(processes);
                });

        from("direct:getTopCPU")
                .process(exchange -> {
                    List<Map<String, Object>> processes = mockService.getProcesses();
                    processes.sort((p1, p2) -> Double.compare((Double)p2.get("cpu"), (Double)p1.get("cpu")));
                    exchange.getMessage().setBody(processes.subList(0, Math.min(10, processes.size())));
                });

        from("direct:getTopMemory")
                .process(exchange -> {
                    List<Map<String, Object>> processes = mockService.getProcesses();
                    processes.sort((p1, p2) -> Double.compare((Double)p2.get("memory"), (Double)p1.get("memory")));
                    exchange.getMessage().setBody(processes.subList(0, Math.min(10, processes.size())));
                });

        from("direct:killProcess")
                .process(exchange -> {
                    exchange.getMessage().setBody(Map.of(
                        "status", "success",
                        "message", "Process killed (mock)"
                    ));
                });
    }
}