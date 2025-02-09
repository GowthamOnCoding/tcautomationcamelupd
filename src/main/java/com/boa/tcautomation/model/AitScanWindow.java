package com.boa.tcautomation.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "ait_scan_window")
public class AitScanWindow {

    @Id
    private String aitNo;
    private String dbType;
    private String scanDay;
    private String startTimeEst;
    private String endTimeEst;
    private LocalDateTime lastUpdated;
    private String lastUpdatedUser;
    private String profile;

    // Getters and Setters
    // ...
}
