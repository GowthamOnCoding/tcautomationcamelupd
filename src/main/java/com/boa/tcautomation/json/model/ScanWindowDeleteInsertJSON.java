package com.boa.tcautomation.json.model;

import lombok.Data;

@Data
public class ScanWindowDeleteInsertJSON {
    private String aitNumber;
    private String dbType;
    private String profile;
    private String expectedOutput;

    public String getAitNumber() {
        return aitNumber;
    }

    public void setAitNumber(String aitNumber) {
        this.aitNumber = aitNumber;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getExpectedOutput() {
        return expectedOutput;
    }

    public void setExpectedOutput(String expectedOutput) {
        this.expectedOutput = expectedOutput;
    }
}
