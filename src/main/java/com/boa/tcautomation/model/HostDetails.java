package com.boa.tcautomation.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "HOST_DETAILS")
public class HostDetails {

    @Id
    @Column(name = "HOST_NAME", nullable = false, length = 30)
    private String hostName;

    @Column(name = "HOST_FULL_NAME", nullable = false, length = 100)
    private String hostFullName;

    @Column(name = "TOTAL_CPU", nullable = true)
    private Integer totalCpu;

    @Column(name = "TOTAL_DISK_SPACE_GB", nullable = true)
    private Integer totalDiskSpaceGb;

    @Column(name = "PROFILE", nullable = false, length = 20)
    private String profile;

    @Column(name = "IS_ACTIVE", nullable = true)
    private Boolean isActive;

    @Column(name = "LAST_UPDATED", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;

    @Column(name = "TOTAL_RAM_GB", nullable = true)
    private Integer totalRamGb;

    @Column(name = "CATEGORY", nullable = true, length = 10)
    private String category;

    @Column(name = "LAST_UPDATED_USER", nullable = true, length = 80)
    private String lastUpdatedUser;

    @Column(name = "PROCESS", nullable = false, length = 100)
    private String process;

    // Getters and Setters
    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getHostFullName() {
        return hostFullName;
    }

    public void setHostFullName(String hostFullName) {
        this.hostFullName = hostFullName;
    }

    public Integer getTotalCpu() {
        return totalCpu;
    }

    public void setTotalCpu(Integer totalCpu) {
        this.totalCpu = totalCpu;
    }

    public Integer getTotalDiskSpaceGb() {
        return totalDiskSpaceGb;
    }

    public void setTotalDiskSpaceGb(Integer totalDiskSpaceGb) {
        this.totalDiskSpaceGb = totalDiskSpaceGb;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Integer getTotalRamGb() {
        return totalRamGb;
    }

    public void setTotalRamGb(Integer totalRamGb) {
        this.totalRamGb = totalRamGb;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLastUpdatedUser() {
        return lastUpdatedUser;
    }

    public void setLastUpdatedUser(String lastUpdatedUser) {
        this.lastUpdatedUser = lastUpdatedUser;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }
}
