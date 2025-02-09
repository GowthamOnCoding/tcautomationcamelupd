package com.boa.tcautomation.json.model;

import lombok.Data;

//@Data
public class ResetDbpropAndEnableToolsJSON {
    private String aitNumber;
    private String configId;
    private String enableTools;
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

    public String getEnableTools() {
        return enableTools;
    }

    public void setEnableTools(String enableTools) {
        this.enableTools = enableTools;
    }

    public String getExpectedOutput() {
        return expectedOutput;
    }

    public void setExpectedOutput(String expectedOutput) {
        this.expectedOutput = expectedOutput;
    }
}
