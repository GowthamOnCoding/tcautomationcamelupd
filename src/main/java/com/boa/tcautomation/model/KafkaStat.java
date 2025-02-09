package com.boa.tcautomation.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "kafka_stat")
public class KafkaStat {

    @Id
    private String aitNo;
    private LocalDateTime startTime;
    private String event;
    private String processNo;
    private String tableName;
    private Long totalRows;
    private Long totalFftInstances;
    private Integer totalMessages;
    private LocalDateTime endTime;
    private Double duration;
    private int seqNo;
    private String remarks;
    private LocalDateTime lastUpdated;
    private String status;
    private String topicName;
    private String groupId;
    private String dbType;
    private String schemaName;
    private String machineName;
    private String profile;
    private String configId;
    private String lastUpdatedUser;
    private String schedulerId;

    // Getters and Setters
    // ...
}
