package com.boa.tcautomation.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tc_steps")
public class TcSteps {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int stepId;
    private String tcId;
    private String stepName;
    @Column(columnDefinition = "TEXT")
    private String parameters;
    private int sequenceNo;
    private String status;

    // Getters and Setters
    public Integer getStepId() {
        return stepId;
    }

    public void setStepId(int stepId) {
        this.stepId = stepId;
    }

    public String getTcId() {
        return tcId;
    }

    public void setTcId(String tcId) {
        this.tcId = tcId;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public int getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(int sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
