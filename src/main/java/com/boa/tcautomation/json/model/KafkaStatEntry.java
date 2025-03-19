
package com.boa.tcautomation.json.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class KafkaStatEntry {
    private String aitNo;
    private String configId;
    private String event;
    private String status;
    private LocalDateTime startTime;
    private LocalDateTime lastUpdated;

    public String getAitNo() {
        return aitNo;
    }

    public void setAitNo(String aitNo) {
        this.aitNo = aitNo;
    }

    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
