package com.boa.tcautomation.model;

import jakarta.persistence.*;

@Entity
@Table(name = "AIT_DBPROP")
public class AitDbProp {
    
    @Column(name = "AIT_NO", length = 20)
    private String aitNo;
    
    @Id
    @Column(name = "ID", length = 10)
    private String id;
    
    @Column(name = "PROFILE", length = 20)
    private String profile;
    
    @Column(name = "DB_TYPE", length = 20)
    private String dbType;
    
    @Column(name = "MACHINE_NAME", length = 200)
    private String machineName;
    
    @Column(name = "DB_NAME", length = 100)
    private String dbName;
    
    @Column(name = "SCHEMA_NAME", length = 100)
    private String schemaName;
    
    @Column(name = "USER_ID", length = 100)
    private String userId;
    
    @Column(name = "PASS_WORD", length = 100)
    private String password;
    
    @Column(name = "TOPIC_NAME", length = 100)
    private String topicName;
    
    @Column(name = "JDBC_URL", length = 400)
    private String jdbcUrl;
    
    @Column(name = "NO_OF_CONNECTION")
    private Integer numberOfConnections;
    
    @Column(name = "EMAIL_ID", length = 100)
    private String emailId;
    
    @Column(name = "IS_ACTIVE", nullable = false)
    private boolean isActive;
    
    @Column(name = "LAST_UPDATED_USER", length = 80)
    private String lastUpdatedUser;
    
    @Column(name = "SDD_Active", nullable = false)
    private boolean sddActive;
    
    @Column(name = "AIML_IS_ACTIVE", nullable = false)
    private boolean aimlIsActive;
    
    @Column(name = "TABLE_LIST", columnDefinition = "varchar(MAX)")
    private String tableList;
    
    @Column(name = "EXEC_STATUS", length = 10)
    private String execStatus;
    
    @Column(name = "AIT_CADENCE", length = 20)
    private String aitCadence;
    
    @Column(name = "IDW_SDD", nullable = false)
    private boolean idwSdd;
    
    @Column(name = "IDW_UDD", nullable = false)
    private boolean idwUdd;
    
    @Column(name = "IEDPS_SDD", nullable = false)
    private boolean iedpsSdd;
    
    @Column(name = "REPORT_TOPIC_NAME", length = 100)
    private String reportTopicName;
    
    @Column(name = "FUNNEL_UDD", nullable = false)
    private boolean funnelUdd;
    
    @Column(name = "FUNNEL_SDD", nullable = false)
    private boolean funnelSdd;
    
    @Column(name = "AIML_SDD", nullable = false)
    private boolean aimlSdd;
    
    @Column(name = "AIML_UDD", nullable = false)
    private boolean aimlUdd;
    
    @Column(name = "FUNNEL_DESTINATION", length = 20)
    private String funnelDestination;
    
    @Column(name = "FUNNEL_DISCOVERY", nullable = false)
    private boolean funnelDiscovery;
    
    @Column(name = "AIML_DISCOVERY", nullable = false)
    private boolean aimlDiscovery;
    
    @Column(name = "IDW_DISCOVERY", nullable = false)
    private boolean idwDiscovery;
    
    @Column(name = "IEDPS_DISCOVERY", nullable = false)
    private boolean iedpsDiscovery;
    
    @Column(name = "AIML VALIDATION", nullable = false)
    private boolean aimlValidation;
    
    @Column(name = "IDW_VALIDATION", nullable = false)
    private boolean idwValidation;
    
    @Column(name = "FFT_DESTINATION", length = 50)
    private String fftDestination;
    
    @Column(name = "AIT_NUM")
    private Integer aitNum;
    
    @Column(name = "JDBC_CON_STR", length = 400)
    private String jdbcConnectionString;
    
    @Column(name = "LOB", length = 20)
    private String lob;
    
    @Column(name = "ENVIRONMENT", length = 20)
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

    public Integer getNumberOfConnections() {
        return numberOfConnections;
    }

    public void setNumberOfConnections(Integer numberOfConnections) {
        this.numberOfConnections = numberOfConnections;
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

    public boolean isIdwValidation() {
        return idwValidation;
    }

    public void setIdwValidation(boolean idwValidation) {
        this.idwValidation = idwValidation;
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

    public String getJdbcConnectionString() {
        return jdbcConnectionString;
    }

    public void setJdbcConnectionString(String jdbcConnectionString) {
        this.jdbcConnectionString = jdbcConnectionString;
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
