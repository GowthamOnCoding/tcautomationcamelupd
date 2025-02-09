package com.boa.tcautomation.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "STEP_CONFIG")
public class StepConfiguration {
    @Id
    private String stepName;
    @Column(columnDefinition = "TEXT")
    private String parameterSchema;
    private Integer timeoutSeconds;
    private Integer maxRetries;
    private String description;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    // Getters, setters, and constructor
}
