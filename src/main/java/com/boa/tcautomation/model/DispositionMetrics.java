package com.boa.tcautomation.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "DP_KTB_MV_DispositionMetrics", schema = "epic")
public class DispositionMetrics {

    @Id
   // @GeneratedValue(strategy = GenerationType.IDENTITY) // Assuming AppID is unique and auto-generated
    @Column(name = "AppID", nullable = true)
    private Integer appId;

    @Column(name = "KTBID", nullable = true)
    private Integer ktbId;

    @Column(name = "DBType", length = 50, nullable = true)
    private String dbType;

    @Column(name = "TotalCount", nullable = true)
    private Integer totalCount;

    @Column(name = "CompletedCount", nullable = true)
    private Integer completedCount;

    @Column(name = "FalsePositiveCount", nullable = true)
    private Integer falsePositiveCount;

    @Column(name = "UDDCount", nullable = true)
    private Integer uddCount;

    @Column(name = "ProvidedBy", length = 50, nullable = true)
    private String providedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ProvidedOn", nullable = true)
    private Date providedOn;

    @Column(name = "DispositionOrderBy", nullable = true)
    private Integer dispositionOrderBy;

    @Column(name = "IsActive", nullable = true)
    private Integer isActive;

    @Column(name = "SustainId", nullable = true)
    private Integer sustainId;

    @Column(name = "SustainOrder", nullable = true)
    private Integer sustainOrder;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ValidationTimeStart", nullable = true)
    private Date validationTimeStart;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ValidationTimeEnd", nullable = true)
    private Date validationTimeEnd;

    @Column(name = "DiscoveryType", length = 20, nullable = true)
    private String discoveryType;

    // Getters and Setters
    public Integer getAppId() { return appId; }
    public void setAppId(Integer appId) { this.appId = appId; }

    public Integer getKtbId() { return ktbId; }
    public void setKtbId(Integer ktbId) { this.ktbId = ktbId; }

    public String getDbType() { return dbType; }
    public void setDbType(String dbType) { this.dbType = dbType; }

    public Integer getTotalCount() { return totalCount; }
    public void setTotalCount(Integer totalCount) { this.totalCount = totalCount; }

    public Integer getCompletedCount() { return completedCount; }
    public void setCompletedCount(Integer completedCount) { this.completedCount = completedCount; }

    public Integer getFalsePositiveCount() { return falsePositiveCount; }
    public void setFalsePositiveCount(Integer falsePositiveCount) { this.falsePositiveCount = falsePositiveCount; }

    public Integer getUddCount() { return uddCount; }
    public void setUddCount(Integer uddCount) { this.uddCount = uddCount; }

    public String getProvidedBy() { return providedBy; }
    public void setProvidedBy(String providedBy) { this.providedBy = providedBy; }

    public Date getProvidedOn() { return providedOn; }
    public void setProvidedOn(Date providedOn) { this.providedOn = providedOn; }

    public Integer getDispositionOrderBy() { return dispositionOrderBy; }
    public void setDispositionOrderBy(Integer dispositionOrderBy) { this.dispositionOrderBy = dispositionOrderBy; }

    public Integer getIsActive() { return isActive; }
    public void setIsActive(Integer isActive) { this.isActive = isActive; }

    public Integer getSustainId() { return sustainId; }
    public void setSustainId(Integer sustainId) { this.sustainId = sustainId; }

    public Integer getSustainOrder() { return sustainOrder; }
    public void setSustainOrder(Integer sustainOrder) { this.sustainOrder = sustainOrder; }

    public Date getValidationTimeStart() { return validationTimeStart; }
    public void setValidationTimeStart(Date validationTimeStart) { this.validationTimeStart = validationTimeStart; }

    public Date getValidationTimeEnd() { return validationTimeEnd; }
    public void setValidationTimeEnd(Date validationTimeEnd) { this.validationTimeEnd = validationTimeEnd; }

    public String getDiscoveryType() { return discoveryType; }
    public void setDiscoveryType(String discoveryType) { this.discoveryType = discoveryType; }
}

