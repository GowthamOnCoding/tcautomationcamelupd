package com.boa.tcautomation.service;

import com.boa.tcautomation.json.model.SocketClientStatus;
import com.boa.tcautomation.util.SshUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SocketClientService {

    private final Map<String, SocketClientStatus> activeClients = new ConcurrentHashMap<>();
    private final SshUtil sshService;

    @Autowired
    public SocketClientService(SshUtil sshService) {
        this.sshService = sshService;
    }

    public void startSocketClient(String host, String processInfo) {
        SocketClientStatus status = new SocketClientStatus();
        status.setRunning(true);
        status.setHost(host);
        status.setPid(processInfo.trim()); // Assuming processInfo contains the PID
        status.setStartTime(LocalDateTime.now());
        activeClients.put(host, status);
    }

    public void stopSocketClient(String host) throws Exception {
        SocketClientStatus status = activeClients.get(host);
        if (status != null && status.isRunning()) {
            // Kill the process using SSH
            sshService.executeCommand(host, "kill -9 " + status.getPid());
            activeClients.remove(host);
        }
    }

    public SocketClientStatus getStatus(String host) {
        SocketClientStatus status = activeClients.get(host);
        if (status == null) {
            status = new SocketClientStatus();
            status.setRunning(false);
            status.setHost(host);
        }

        // If running, check if process is still alive
        if (status.isRunning()) {
            try {
                String result = sshService.executeCommand(host, "ps -p " + status.getPid());
                if (!result.contains(status.getPid())) {
                    status.setRunning(false);
                    activeClients.remove(host);
                } else {
                    // Get latest output
                    String output = sshService.executeCommand(host, "tail -n 50 /tmp/socket_client.log");
                    status.setLastOutput(output);
                }
            } catch (Exception e) {
                status.setRunning(false);
                activeClients.remove(host);
            }
        }

        return status;
    }
}
