package com.boa.tcautomation.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

//@Data
@Entity
@Table(name = "ait_dbprop")
public class AitDbProp {

    @Id
    private String aitNo;
    private String id;
    private String profile;
    private String dbType;
    private String machineName;
    private String dbName;
    private String schemaName;
    private String userId;
    private String password;
    private String topicName;
    private String jdbcUrl;
    private Integer noOfConnection;
    private String emailId;
    private boolean isActive;
    private String lastUpdatedUser;
    private boolean sddActive;
    private boolean aimlIsActive;
    private String tableList;
    private String execStatus;
    private String aitCadence;
    private boolean idwSdd;
    private boolean idwUdd;
    private boolean iedpsSdd;
    private String reportTopicName;
    private boolean funnelUdd;
    private boolean funnelSdd;
    private boolean aimlSdd;
    private boolean aimlUdd;
    private String funnelDestination;
    private boolean funnelDiscovery;
    private boolean aimlDiscovery;
    private boolean idwDiscovery;
    private boolean iedpsDiscovery;
    private boolean aimlValidation;
    private String fftDestination;
    private Integer aitNum;
    private String jdbcConStr;
    private String lob;
    private String environment;

    // Getters and Setters
    public String getAitNo() {
        return aitNo;
    }

    public void setAitNo(String aitNo) {
        this.aitNo = aitNo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public Integer getNoOfConnection() {
        return noOfConnection;
    }

    public void setNoOfConnection(Integer noOfConnection) {
        this.noOfConnection = noOfConnection;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getLastUpdatedUser() {
        return lastUpdatedUser;
    }

    public void setLastUpdatedUser(String lastUpdatedUser) {
        this.lastUpdatedUser = lastUpdatedUser;
    }

    public boolean isSddActive() {
        return sddActive;
    }

    public void setSddActive(boolean sddActive) {
        this.sddActive = sddActive;
    }

    public boolean isAimlIsActive() {
        return aimlIsActive;
    }

    public void setAimlIsActive(boolean aimlIsActive) {
        this.aimlIsActive = aimlIsActive;
    }

    public String getTableList() {
        return tableList;
    }

    public void setTableList(String tableList) {
        this.tableList = tableList;
    }

    public String getExecStatus() {
        return execStatus;
    }

    public void setExecStatus(String execStatus) {
        this.execStatus = execStatus;
    }

    public String getAitCadence() {
        return aitCadence;
    }

    public void setAitCadence(String aitCadence) {
        this.aitCadence = aitCadence;
    }

    public boolean isIdwSdd() {
        return idwSdd;
    }

    public void setIdwSdd(boolean idwSdd) {
        this.idwSdd = idwSdd;
    }

    public boolean isIdwUdd() {
        return idwUdd;
    }

    public void setIdwUdd(boolean idwUdd) {
        this.idwUdd = idwUdd;
    }

    public boolean isIedpsSdd() {
        return iedpsSdd;
    }

    public void setIedpsSdd(boolean iedpsSdd) {
        this.iedpsSdd = iedpsSdd;
    }

    public String getReportTopicName() {
        return reportTopicName;
    }

    public void setReportTopicName(String reportTopicName) {
        this.reportTopicName = reportTopicName;
    }

    public boolean isFunnelUdd() {
        return funnelUdd;
    }

    public void setFunnelUdd(boolean funnelUdd) {
        this.funnelUdd = funnelUdd;
    }

    public boolean isFunnelSdd() {
        return funnelSdd;
    }

    public void setFunnelSdd(boolean funnelSdd) {
        this.funnelSdd = funnelSdd;
    }

    public boolean isAimlSdd() {
        return aimlSdd;
    }

    public void setAimlSdd(boolean aimlSdd) {
        this.aimlSdd = aimlSdd;
    }

    public boolean isAimlUdd() {
        return aimlUdd;
    }

    public void setAimlUdd(boolean aimlUdd) {
        this.aimlUdd = aimlUdd;
    }

    public String getFunnelDestination() {
        return funnelDestination;
    }

    public void setFunnelDestination(String funnelDestination) {
        this.funnelDestination = funnelDestination;
    }

    public boolean isFunnelDiscovery() {
        return funnelDiscovery;
    }

    public void setFunnelDiscovery(boolean funnelDiscovery) {
        this.funnelDiscovery = funnelDiscovery;
    }

    public boolean isAimlDiscovery() {
        return aimlDiscovery;
    }

    public void setAimlDiscovery(boolean aimlDiscovery) {
        this.aimlDiscovery = aimlDiscovery;
    }

    public boolean isIdwDiscovery() {
        return idwDiscovery;
    }

    public void setIdwDiscovery(boolean idwDiscovery) {
        this.idwDiscovery = idwDiscovery;
    }

    public boolean isIedpsDiscovery() {
        return iedpsDiscovery;
    }

    public void setIedpsDiscovery(boolean iedpsDiscovery) {
        this.iedpsDiscovery = iedpsDiscovery;
    }

    public boolean isAimlValidation() {
        return aimlValidation;
    }

    public void setAimlValidation(boolean aimlValidation) {
        this.aimlValidation = aimlValidation;
    }

    public String getFftDestination() {
        return fftDestination;
    }

    public void setFftDestination(String fftDestination) {
        this.fftDestination = fftDestination;
    }

    public Integer getAitNum() {
        return aitNum;
    }

    public void setAitNum(Integer aitNum) {
        this.aitNum = aitNum;
    }

    public String getJdbcConStr() {
        return jdbcConStr;
    }

    public void setJdbcConStr(String jdbcConStr) {
        this.jdbcConStr = jdbcConStr;
    }

    public String getLob() {
        return lob;
    }

    public void setLob(String lob) {
        this.lob = lob;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }
}
