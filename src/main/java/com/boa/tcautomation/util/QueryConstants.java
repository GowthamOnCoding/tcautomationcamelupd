package com.boa.tcautomation.util;

import com.fasterxml.jackson.databind.deser.impl.BeanPropertyMap;

public class QueryConstants {
    public static final String SELECT_ACTIVE_TC_MASTERS = "SELECT * FROM testdb.tc_master WHERE is_active = 1";
    public static final String SELECT_AIT_DB_PROPS = "SELECT * FROM AIT_DBPROP where AIT_NO='<AIT_NO>'";
    public static final String DELETE_AIT_SCAN_WINDOW = "DELETE FROM ait_scan_window WHERE ait_no = '<AIT_NO>' AND db_type = '<DB_TYPE>'";
    public static final String INSERT_AIT_SCAN_WINDOW = "INSERT INTO ait_scan_window (ait_no, db_type, scan_day, start_time_est, end_time_est, last_updated, last_updated_user, profile) VALUES " +
            "('<AIT_NO>', '<DB_TYPE>', 'MON', '00:00', '23:59', CURRENT_TIMESTAMP, 'system', 'public')," +
            "('<AIT_NO>', '<DB_TYPE>', 'TUE', '00:00', '23:59', CURRENT_TIMESTAMP, 'system', 'public')," +
            "('<AIT_NO>', '<DB_TYPE>', 'WED', '00:00', '23:59', CURRENT_TIMESTAMP, 'system', 'public')," +
            "('<AIT_NO>', '<DB_TYPE>', 'THU', '00:00', '23:59', CURRENT_TIMESTAMP, 'system', 'public')," +
            "('<AIT_NO>', '<DB_TYPE>', 'FRI', '00:00', '23:59', CURRENT_TIMESTAMP, 'system', 'public')," +
            "('<AIT_NO>', '<DB_TYPE>', 'SAT', '00:00', '23:59', CURRENT_TIMESTAMP, 'system', 'public')," +
            "('<AIT_NO>', '<DB_TYPE>', 'SUN', '00:00', '23:59', CURRENT_TIMESTAMP, 'system', 'public')";
    public static final String SELECT_TC_STEPS_BY_TC_ID = "SELECT * FROM tc_steps WHERE TC_ID='<TC_ID>' ORDER BY SEQUENCE_NO";
    public static final String SELECT_PARAMETER_SCHEMA = "SELECT * FROM PARAMETER_SCHEMA WHERE SCHEMA_ID='<SCHEMA_ID>'";
    public static final String SELECT_STEP_CONFIG_BY_STEP_NAME = "SELECT * FROM step_config WHERE step_name='<STEP_NAME>'";
    public static final String RESET_DBPROP_QUERY = "UPDATE AIT_DBPROP SET FUNNEL_DISCOVERY=0,IDW_DISCOVERY=0,IEDPS_DISCOVERY=0,AIML_DISCOVERY=0 WHERE AIT_NO='<AIT_NO>' AND ID IN (<CONFIG_ID>)";
    public static final String ENABLE_TOOLS_QUERY = "UPDATE AIT_DBPROP SET " +
            "FUNNEL_DISCOVERY = CASE WHEN <ENABLE_FUNNEL> THEN 1 ELSE FUNNEL_DISCOVERY END, " +
            "IDW_DISCOVERY = CASE WHEN <ENABLE_IDW> THEN 1 ELSE IDW_DISCOVERY END, " +
            "IEDPS_DISCOVERY = CASE WHEN <ENABLE_IEDPS> THEN 1 ELSE IEDPS_DISCOVERY END, " +
            "AIML_DISCOVERY = CASE WHEN <ENABLE_AIML> THEN 1 ELSE AIML_DISCOVERY END " +
            "WHERE AIT_NO = '<AIT_NO>' AND ID IN (<CONFIG_ID>)";
    public static final String DELETE_KAFKA_STAT = "DELETE FROM KAFKA_STAT WHERE AIT_NO = ?";
    public static final String SELECT_QUERY_TEMPLATE = "SELECT * FROM <TABLE_NAME> WHERE AIT_NO = '<AIT_NO>' AND CONFIG_ID in (<CONFIG_ID>)";

}


