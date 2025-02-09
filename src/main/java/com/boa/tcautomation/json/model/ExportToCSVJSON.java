package com.boa.tcautomation.json.model;

import lombok.Data;

@Data
public class ExportToCSVJSON {
    private String aitNumber;
    private String configId;
    private String tableName;
    private String whereCondition;
    private String expectedOutput;

    // Getters and Setters
    public String getAitNumber() {
        return aitNumber;
    }

    public void setAitNumber(String aitNumber) {
        this.aitNumber = aitNumber;
    }

    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getWhereCondition() {
        return whereCondition;
    }

    public void setWhereCondition(String whereCondition) {
        this.whereCondition = whereCondition;
    }

    public String getExpectedOutput() {
        return expectedOutput;
    }

    public void setExpectedOutput(String expectedOutput) {
        this.expectedOutput = expectedOutput;
    }
}
