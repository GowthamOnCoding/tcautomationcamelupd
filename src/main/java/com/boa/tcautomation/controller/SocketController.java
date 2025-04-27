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

    @PostMapping("/run/{host}")
    public ResponseEntity<?> runSocketClient(@PathVariable String host) {
        try {
            // Fixed command to run your socket client script
            String command = "/path/to/your/socket_client.sh";

            // Execute via SSH
            String result = sshService.executeCommand(host, command);

            // Store the process information
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

    @PostMapping("/stop/{host}")
    public ResponseEntity<?> stopSocketClient(@PathVariable String host) {
        try {
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
}
