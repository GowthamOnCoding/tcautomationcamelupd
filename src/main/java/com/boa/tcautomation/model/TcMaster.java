package com.boa.tcautomation.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tc_master")
public class TcMaster {
    @Id
    @Column(name = "tc_id")
    private String tcId;
    
    private String tcName;
    
    private String description;
    
    private String createdBy;
    
    @Column(updatable = false)
    private LocalDateTime createdDate;
    
    private String modifiedBy;
    
    private LocalDateTime modifiedDate;
    
    @Column(nullable = false, columnDefinition = "bit")
    private String isActive = "true";
    
    private String aitNo;
    
    private String configIds;

    // Getters and Setters
    public String getTcId() {
        return tcId;
    }

    public void setTcId(String tcId) {
        this.tcId = tcId;
    }

    public String getTcName() {
        return tcName;
    }

    public void setTcName(String tcName) {
        this.tcName = tcName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(LocalDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public Boolean isActiveAsBool() {
        return "true".equalsIgnoreCase(isActive);
    }

    public void setIsActiveAsBool(Boolean active) {
        this.isActive = active != null ? active.toString() : "false";
    }

    public String getAitNo() {
        return aitNo;
    }

    public void setAitNo(String aitNo) {
        this.aitNo = aitNo;
    }

    public String getConfigIds() {
        return configIds;
    }

    public void setConfigIds(String configIds) {
        this.configIds = configIds;
    }
}
