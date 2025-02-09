package com.boa.tcautomation.json.model;

import lombok.Data;

@Data
public class ScanWindowDeleteInsertJSON {
    private String aitNumber;
    private String dbType;
    private String profile;
    private String expectedOutput;
}
