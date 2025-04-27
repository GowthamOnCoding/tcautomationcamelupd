package com.boa.tcautomation.controller;

import com.boa.tcautomation.json.model.SocketClientRequest;
import com.boa.tcautomation.json.model.SocketClientStatus;
import com.boa.tcautomation.service.SocketClientService;
import com.boa.tcautomation.util.SshUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/socket")
public class SocketController {

    private final SocketClientService socketClientService;
    private final SshUtil sshService;

    @Autowired
    public SocketController(SocketClientService socketClientService, SshUtil sshService) {
        this.socketClientService = socketClientService;
        this.sshService = sshService;
    }

    @PostMapping("/run")
    public ResponseEntity<?> runSocketClient(@RequestBody SocketClientRequest request) {
        try {
            String host = request.getHost();
            Map<String, String> params = request.getParams();

            // Build the command to run your socket client script
            String command = buildSocketClientCommand(params);

            // Execute via SSH
            String result = sshService.executeCommand(host, command);

            // Store the process information if needed
            socketClientService.startSocketClient(host, result);

            return ResponseEntity.ok().body(Map.of(
                    "status", "success",
                    "message", "Socket client started successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/stop")
    public ResponseEntity<?> stopSocketClient(@RequestBody Map<String, String> request) {
        try {
            String host = request.get("host");
            socketClientService.stopSocketClient(host);
            return ResponseEntity.ok().body(Map.of(
                    "status", "success",
                    "message", "Socket client stopped successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/status/{host}")
    public ResponseEntity<?> getSocketClientStatus(@PathVariable String host) {
        try {
            SocketClientStatus status = socketClientService.getStatus(host);
            return ResponseEntity.ok().body(status);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    private String buildSocketClientCommand(Map<String, String> params) {
        // Customize this to match your actual socket client script and parameters
        StringBuilder command = new StringBuilder("/path/to/your/socket_client.sh");

        if (params != null) {
            if (params.containsKey("port")) {
                command.append(" -p ").append(params.get("port"));
            }
            if (params.containsKey("timeout")) {
                command.append(" -t ").append(params.get("timeout"));
            }
            // Add other parameters as needed
        }

        // Add nohup and redirect output if you want it to run in background
        return "nohup " + command + " > /tmp/socket_client.log 2>&1 & echo $!";
    }
}
