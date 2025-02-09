package com.boa.tcautomation.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tc_execution_log")
public class TcExecutionLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long executionId;
    
    @Column(name = "tc_id")
    private String tcId;
    
    private Integer stepId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    @Column(columnDefinition = "TEXT")
    private String errorMessage;
}
