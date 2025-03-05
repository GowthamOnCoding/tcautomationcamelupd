package com.boa.tcautomation.util;

import java.util.HashMap;
import java.util.Map;

public class Constants {
    public static final String INPROGRESS ="In Progress";
    public static final String COMPLETED ="Completed";
    public static final String FAILED ="Failed";
    public static final Map<String, String> TC_EXEC_FIELD_TO_COLUMN_MAP = new HashMap<>();

    static {
        TC_EXEC_FIELD_TO_COLUMN_MAP.put("executionId", "EXECUTION_ID");
        TC_EXEC_FIELD_TO_COLUMN_MAP.put("tcId", "TC_ID");
        TC_EXEC_FIELD_TO_COLUMN_MAP.put("stepId", "STEP_ID");
        TC_EXEC_FIELD_TO_COLUMN_MAP.put("startTime", "START_TIME");
        TC_EXEC_FIELD_TO_COLUMN_MAP.put("endTime", "END_TIME");
        TC_EXEC_FIELD_TO_COLUMN_MAP.put("status", "STATUS");
        TC_EXEC_FIELD_TO_COLUMN_MAP.put("errorMessage", "ERROR_MESSAGE");
    }
}
