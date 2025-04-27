package com.boa.tcautomation.json.model;

import java.time.LocalDateTime;

public class SocketClientStatus {
    private boolean running;
    private String host;
    private String pid;
    private LocalDateTime startTime;
    private String lastOutput;

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public String getLastOutput() {
        return lastOutput;
    }

    public void setLastOutput(String lastOutput) {
        this.lastOutput = lastOutput;
    }
// Getters and setters
}
