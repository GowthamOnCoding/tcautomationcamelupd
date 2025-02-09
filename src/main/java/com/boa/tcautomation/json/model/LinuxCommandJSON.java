package com.boa.tcautomation.json.model;

import lombok.Data;

//@Data
public class LinuxCommandJSON {
    private String hostname;
    private String process;
    private String reqType;
    private String command;
    private String bgFlag;
    private String expectedOutput;

    // Getters and Setters
    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getReqType() {
        return reqType;
    }

    public void setReqType(String reqType) {
        this.reqType = reqType;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getBgFlag() {
        return bgFlag;
    }

    public void setBgFlag(String bgFlag) {
        this.bgFlag = bgFlag;
    }

    public String getExpectedOutput() {
        return expectedOutput;
    }

    public void setExpectedOutput(String expectedOutput) {
        this.expectedOutput = expectedOutput;
    }
}
