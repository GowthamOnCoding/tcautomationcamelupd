package com.boa.tcautomation.util;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class REConstants {

    private REConstants() {}

    // ===== Dialect switch (artifact SQL syntax only) =====
    public static final String DIALECT = "MYSQL"; // or "SQLSERVER"

    // ===== Defaults & paths =====
    public static final Path   ARTIFACT_ROOT    = Paths.get("C:", "data", "artifacts"); // change to Paths.get("/data","artifacts") on Linux
    public static final String DEFAULT_PROFILE  = "public";
    public static final String DEFAULT_USER     = "tca";
    public static final int    START_TC_SEQ     = 0;       // tc_sequence seed when missing
    public static final int    START_AIT_SEQ    = 10000;   // tc_sequence seed for AIT when missing

    // ===== AIT prefix rule for kafka_stat =====
    public static final String AIT_PREFIX_SDD     = "SDD_AIT_";
    public static final String AIT_PREFIX_DEFAULT = "AIT_";

    // ===== tc_steps names & params =====
    public static final String STEP_CLEAN     = "cleanAndInsertTestData";
    public static final String STEP_EXPORT    = "exportTableToCsv";
    public static final String STEP_RUN_SCHED = "runScheduler";
    public static final String STEP_DELAY     = "introduceDelay";
    public static final String STEP_VALIDATE  = "executeAndValidateQuery";

    public static final String STEP_EXPORT_BEFORE_PARAM = "TABLES_BEFORE";
    public static final String STEP_EXPORT_AFTER_PARAM  = "TABLES_AFTER";
    public static final String STEP_RUN_SCHED_PARAM     = "";
    public static final String STEP_DELAY_PARAM         = "10000";   // ms

    // ===== DB queries (used by runner via JdbcTemplate) =====
    public static final String QRY_SELECT_SEQ_FMT =
            "SELECT value FROM tc_sequence WHERE id='%s'";
    public static final String QRY_INIT_SEQ_FMT   =
            "INSERT INTO tc_sequence (id,value) VALUES ('%s', %d)";
    public static final String QRY_UPDATE_SEQ     =
            "UPDATE tc_sequence SET value=? WHERE id=?";

    public static final String QRY_INSERT_TC_MASTER_FMT =
            "INSERT INTO tc_master (TC_ID, tc_name, DESCRIPTION, created_by, CREATED_DATE, config_ids, ait_no, is_active) " +
                    "VALUES ('%s','%s','%s','system', CURRENT_TIMESTAMP, 'CFG_AUTO', '%s', 1)";

    public static final String QRY_INSERT_TC_STEP_FMT =
            "INSERT INTO tc_steps (tc_id, step_name, PARAMETERS, SEQUENCE_NO, status) " +
                    "VALUES ('%s','%s','%s',%d,'PENDING')";

    // ===== Validation target selector (fallback when rules don't specify) =====
    // Allowed values: KAFKA_ONLY | SCHED_ONLY | BOTH
    public static final String VALIDATION_TARGET = "BOTH";

    // ===================== MySQL artifact templates (single-line + ###) =====================
    public static final class MYSQL {
        public static final String HEADER_FMT =
                "-- MySQL artifact for AIT_NO(bare)=%s, DBTYPE=%s, DISC=%s START TRANSACTION;###";
        public static final String FOOTER = "COMMIT;###";

        // DIAC (ait_config)
        public static final String DIAC_FMT =
                "DELETE FROM ait_config WHERE AIT_NO = '%s';### " +
                        "INSERT INTO ait_config (" +
                        "AIT_NO, FUNNEL_SDD, AIML_SDD, IDW_SDD, IEDPS_SDD, ESPIAL_SDD," +
                        "FUNNEL_FFT, IDW_FFT, ESPIAL_FFT, IS_ESPIAL, IS_ACTIVE," +
                        "AIML_VALIDATION, FULL_SCAN, TOPIC_NAME, AIT_CADENCE, REPORT_TOPIC_NAME, FFT_DESTINATION," +
                        "PROFILE, LOB, LAST_UPDATED_USER, LAST_UPDATED) " +
                        "VALUES ('%s', %d, %d, %d, %d, %d, %d, %d, %d, 0, 1, 0, 0, '%s', NULL, NULL, NULL, '%s', NULL, '%s', NOW());###";

        // DIDP (ait_dbprop)
        public static final String DIDP_FMT =
                "DELETE FROM ait_dbprop WHERE AIT_NO = '%s';### " +
                        "INSERT INTO ait_dbprop (" +
                        "AIT_NO, ID, DB_TYPE, MACHINE_NAME, DB_NAME, SCHEMA_NAME, USER_ID, PASS_WORD," +
                        "TOPIC_NAME, JDBC_URL, IS_ACTIVE, LAST_UPDATED_USER, PROFILE, LAST_UPDATED) " +
                        "VALUES ('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s',1,'%s','%s',NOW());###";

        // DIKS (kafka_stat)
        public static final String DIKS_HEADER_FMT =
                "DELETE FROM kafka_stat WHERE AIT_NO = '%s';###";
        public static final String DIKS_ROW_FMT =
                "INSERT INTO kafka_stat (" +
                        "AIT_NO, START_TIME, EVENT, END_TIME, DURATION, SEQ_NO, " +
                        "STATUS, TOPIC_NAME, GROUP_ID, DB_TYPE, SCHEMA_NAME, MACHINE_NAME," +
                        "PROFILE, CONFIG_ID, LAST_UPDATED_USER, SCHEDULERID, LAST_UPDATED) " +
                        "VALUES (" +
                        "'%s','%s','%s','%s',%d,%d," +
                        "'%s','%s','%s','%s','%s','%s'," +
                        "'%s','%s','%s','%s',NOW());###";

        // DIAS (ait_seq)  — SEQ_NO = 1 for the whole testcase
        public static final String DIAS_FMT =
                "DELETE FROM ait_seq WHERE AIT_NO = '%s';### " +
                        "INSERT INTO ait_seq (AIT_NO, SEQ_NO, SCHEMA_NAME, DB_TYPE, CONFIG_ID) " +
                        "VALUES ('%s', 1, '%s', '%s', %s);###";

        // DISW (ait_scan_window) — 7 days 00:00-23:59
        public static final String DISW_DELETE_FMT =
                "DELETE FROM ait_scan_window WHERE AIT_NO = '%s' AND DB_TYPE = '%s';###";
        public static final String DISW_INSERT_FMT =
                "INSERT INTO ait_scan_window (ait_no, db_type, scan_day, start_time_est, end_time_est, last_updated, last_updated_user, profile) VALUES " +
                        "('%s','%s','MON','00:00','23:59',CURRENT_TIMESTAMP,'system','public')," +
                        "('%s','%s','TUE','00:00','23:59',CURRENT_TIMESTAMP,'system','public')," +
                        "('%s','%s','WED','00:00','23:59',CURRENT_TIMESTAMP,'system','public')," +
                        "('%s','%s','THU','00:00','23:59',CURRENT_TIMESTAMP,'system','public')," +
                        "('%s','%s','FRI','00:00','23:59',CURRENT_TIMESTAMP,'system','public')," +
                        "('%s','%s','SAT','00:00','23:59',CURRENT_TIMESTAMP,'system','public')," +
                        "('%s','%s','SUN','00:00','23:59',CURRENT_TIMESTAMP,'system','public');###";

        // DIDM (dp_ktb_mv_dispositionmetrics)
        public static final String DIDM_FMT =
                "DELETE FROM dp_ktb_mv_dispositionmetrics WHERE AppID = %s;### " +
                        "INSERT INTO dp_ktb_mv_dispositionmetrics (" +
                        "AppID, DBType, ProvidedBy, ProvidedOn, IsActive, DiscoveryType) " +
                        "VALUES (" +
                        "%s, '%s', 'system', " +
                        "COALESCE((SELECT DATE_ADD(MAX(LAST_UPDATED), INTERVAL 2 MINUTE) FROM kafka_stat WHERE AIT_NO='%s'), NOW()), " +
                        "1, '%s');###";

        // DKSS — Scheduler stat cleanup
        public static final String DKSS_FMT =
                "DELETE FROM KAFKA_AIT_SCHEDULER_STAT WHERE AIT_NO LIKE '%%%s%%';###";

        // Wrap a boolean expression into TRUE/FALSE
        public static final String WRAP_BOOL_TO_RESULT_FMT =
                "SELECT CASE WHEN (%s) THEN 'TRUE' ELSE 'FALSE' END AS validation_result;###";
    }

    // ===================== SQL Server artifact templates (single-line + ###) =====================
    public static final class SQLSERVER {
        public static final String HEADER_FMT =
                "-- SQL Server artifact for AIT_NO(bare)=%s, DBTYPE=%s, DISC=%s BEGIN TRANSACTION;###";
        public static final String FOOTER = "COMMIT TRANSACTION;###";

        public static final String DIAC_FMT =
                "DELETE FROM ait_config WHERE AIT_NO = '%s';### " +
                        "INSERT INTO ait_config (" +
                        "AIT_NO, FUNNEL_SDD, AIML_SDD, IDW_SDD, IEDPS_SDD, ESPIAL_SDD," +
                        "FUNNEL_FFT, IDW_FFT, ESPIAL_FFT, IS_ESPIAL, IS_ACTIVE," +
                        "AIML_VALIDATION, FULL_SCAN, TOPIC_NAME, AIT_CADENCE, REPORT_TOPIC_NAME, FFT_DESTINATION," +
                        "PROFILE, LOB, LAST_UPDATED_USER, LAST_UPDATED) " +
                        "VALUES ('%s', %d, %d, %d, %d, %d, %d, %d, %d, 0, 1, 0, 0, '%s', NULL, NULL, NULL, '%s', NULL, '%s', GETDATE());###";

        public static final String DIDP_FMT =
                "DELETE FROM ait_dbprop WHERE AIT_NO = '%s';### " +
                        "INSERT INTO ait_dbprop (" +
                        "AIT_NO, ID, DB_TYPE, MACHINE_NAME, DB_NAME, SCHEMA_NAME, USER_ID, PASS_WORD," +
                        "TOPIC_NAME, JDBC_URL, IS_ACTIVE, LAST_UPDATED_USER, PROFILE, LAST_UPDATED) " +
                        "VALUES ('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s',1,'%s','%s',GETDATE());###";

        public static final String DIKS_HEADER_FMT =
                "DELETE FROM kafka_stat WHERE AIT_NO = '%s';###";
        public static final String DIKS_ROW_FMT =
                "INSERT INTO kafka_stat (" +
                        "AIT_NO, START_TIME, EVENT, END_TIME, DURATION, SEQ_NO, " +
                        "STATUS, TOPIC_NAME, GROUP_ID, DB_TYPE, SCHEMA_NAME, MACHINE_NAME," +
                        "PROFILE, CONFIG_ID, LAST_UPDATED_USER, SCHEDULERID, LAST_UPDATED) " +
                        "VALUES (" +
                        "'%s','%s','%s','%s',%d,%d," +
                        "'%s','%s','%s','%s','%s','%s'," +
                        "'%s','%s','%s','%s',GETDATE());###";

        public static final String DIAS_FMT =
                "DELETE FROM ait_seq WHERE AIT_NO = '%s';### " +
                        "INSERT INTO ait_seq (AIT_NO, SEQ_NO, SCHEMA_NAME, DB_TYPE, CONFIG_ID) " +
                        "VALUES ('%s', 1, '%s', '%s', %s);###";

        public static final String DISW_DELETE_FMT =
                "DELETE FROM ait_scan_window WHERE AIT_NO = '%s' AND DB_TYPE = '%s';###";
        public static final String DISW_INSERT_FMT =
                "INSERT INTO ait_scan_window (ait_no, db_type, scan_day, start_time_est, end_time_est, last_updated, last_updated_user, profile) VALUES " +
                        "('%s','%s','MON','00:00','23:59',GETDATE(),'system','public')," +
                        "('%s','%s','TUE','00:00','23:59',GETDATE(),'system','public')," +
                        "('%s','%s','WED','00:00','23:59',GETDATE(),'system','public')," +
                        "('%s','%s','THU','00:00','23:59',GETDATE(),'system','public')," +
                        "('%s','%s','FRI','00:00','23:59',GETDATE(),'system','public')," +
                        "('%s','%s','SAT','00:00','23:59',GETDATE(),'system','public')," +
                        "('%s','%s','SUN','00:00','23:59',GETDATE(),'system','public');###";

        public static final String DIDM_FMT =
                "DELETE FROM dp_ktb_mv_dispositionmetrics WHERE AppID = %s;### " +
                        "INSERT INTO dp_ktb_mv_dispositionmetrics (" +
                        "AppID, DBType, ProvidedBy, ProvidedOn, IsActive, DiscoveryType) " +
                        "VALUES (" +
                        "%s, '%s', 'system', " +
                        "ISNULL((SELECT DATEADD(MINUTE, 2, MAX(LAST_UPDATED)) FROM kafka_stat WHERE AIT_NO='%s'), GETDATE()), " +
                        "1, '%s');###";

        public static final String DKSS_FMT =
                "DELETE FROM KAFKA_AIT_SCHEDULER_STAT WHERE AIT_NO LIKE '%%%s%%';###";

        public static final String WRAP_BOOL_TO_RESULT_FMT =
                "SELECT CASE WHEN (%s) THEN 'TRUE' ELSE 'FALSE' END AS validation_result;###";
    }
}
