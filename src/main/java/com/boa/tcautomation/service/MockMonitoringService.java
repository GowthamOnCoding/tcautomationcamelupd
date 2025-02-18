package com.boa.tcautomation.service;

import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class MockMonitoringService {

    public Map<String, Object> getSystemInfo() {
        Map<String, Object> systemInfo = new HashMap<>();

        // Mock CPU Info
        Map<String, Double> cpu = new HashMap<>();
        cpu.put("user", randomDouble(20, 40));
        cpu.put("system", randomDouble(10, 30));
        cpu.put("idle", randomDouble(30, 50));
        systemInfo.put("cpu", cpu);

        // Mock Memory Info
        Map<String, Long> memory = new HashMap<>();
        memory.put("total", 16384L);  // 16GB in MB
        memory.put("used", randomLong(8192L, 14336L));  // 8-14GB in MB
        memory.put("free", randomLong(2048L, 8192L));   // 2-8GB in MB
        systemInfo.put("memory", memory);

        // Mock Disk Info
        List<Map<String, Object>> disks = new ArrayList<>();

        // Root partition
        Map<String, Object> rootDisk = new HashMap<>();
        rootDisk.put("filesystem", "/dev/sda1");
        rootDisk.put("size", "256G");
        rootDisk.put("used", "158G");
        rootDisk.put("available", "98G");
        rootDisk.put("usePercentage", "62%");
        rootDisk.put("mountPoint", "/");

        // Data partition
        Map<String, Object> dataDisk = new HashMap<>();
        dataDisk.put("filesystem", "/dev/sdb1");
        dataDisk.put("size", "1.0T");
        dataDisk.put("used", "756G");
        dataDisk.put("available", "244G");
        dataDisk.put("usePercentage", "76%");
        dataDisk.put("mountPoint", "/data");

        disks.add(rootDisk);
        disks.add(dataDisk);
        systemInfo.put("disk", disks);

        return systemInfo;
    }

    public List<Map<String, Object>> getProcesses() {
        List<Map<String, Object>> processes = new ArrayList<>();
        String[] users = {"root", "tomcat", "mysql", "nginx"};
        String[] commands = {
                "java -jar app.jar",
                "mysqld",
                "nginx: master process",
                "python3 script.py",
                "/usr/sbin/sshd",
                "redis-server",
                "/usr/bin/mongod",
                "node server.js"
        };

        for (int i = 0; i < 20; i++) {
            Map<String, Object> process = new HashMap<>();
            process.put("pid", randomLong(1000L, 9999L));
            process.put("user", users[random(0, users.length - 1)]);
            process.put("cpu", randomDouble(0.1, 15.0));
            process.put("memory", randomDouble(0.1, 8.0));
            process.put("vsz", randomLong(100000L, 999999L));
            process.put("rss", randomLong(10000L, 99999L));
            process.put("tty", "?");
            process.put("stat", "Ssl");
            process.put("start", "May08");
            process.put("time", "0:02");
            process.put("command", commands[random(0, commands.length - 1)]);
            processes.add(process);
        }

        return processes;
    }

    private double randomDouble(double min, double max) {
        return ThreadLocalRandom.current().nextDouble(min, max);
    }

    private long randomLong(long min, long max) {
        return ThreadLocalRandom.current().nextLong(min, max);
    }

    private int random(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}
