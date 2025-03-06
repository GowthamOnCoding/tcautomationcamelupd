package com.boa.tcautomation.service;

import com.boa.tcautomation.model.HostDetails;
import com.boa.tcautomation.repository.HostDetailsRepository;
import com.boa.tcautomation.util.SshUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MonitoringService {

    @Autowired
    private HostDetailsRepository hostDetailsRepository;

    @Autowired
    private SshUtil sshService;

    public List<HostDetails> getAllHosts() {
        return hostDetailsRepository.findByIsActiveTrue();
    }

    public HostDetails getHostByName(String hostName) {
        return hostDetailsRepository.findById(hostName).orElseThrow();
    }

    public Map<String, Object> getSystemInfo(String hostName) throws Exception {
        HostDetails host = getHostByName(hostName);
        Map<String, Object> systemInfo = new HashMap<>();

        // CPU Info
        String cpuInfo = sshService.executeCommand(host.getHostName(), "top -bn1 | grep 'Cpu(s)'");
        systemInfo.put("cpu", parseCpuInfo(cpuInfo));

        // Memory Info
        String memInfo = sshService.executeCommand(host.getHostName(), "free -m");
        systemInfo.put("memory", parseMemoryInfo(memInfo));

        // Disk Info
        String diskInfo = sshService.executeCommand(host.getHostName(), "df -h");
        systemInfo.put("disk", parseDiskInfo(diskInfo));

        // Update last checked time
        host.setLastUpdated(new Date());
        hostDetailsRepository.save(host);

        return systemInfo;
    }

    // Helper methods for parsing command outputs (same as in ServerMonitorRoutes)
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
}
