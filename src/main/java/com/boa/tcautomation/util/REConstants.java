package com.boa.tcautomation.util;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class REConstants {

    private REConstants() {}

    // ===== Dialect switch (controls artifact SQL syntax only) =====
    public static final String DIALECT = "MYSQL"; // or "SQLSERVER"

    // ===== Defaults & paths =====
    public static final Path   ARTIFACT_ROOT    = Paths.get("C:\\", "data", "artifacts");
    public static final String DEFAULT_PROFILE  = "public";
    public static final String DEFAULT_USER     = "tca";
    public static final int    START_TC_SEQ     = 0;
    public static final int    START_AIT_SEQ    = 10000;

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
    public static final String STEP_VALIDATE_PARAM      = "VALIDATION_SQL_OR_FILE";

    // ===== DB queries for sequences, master, steps (single-line + ###) =====
    public static final String QRY_SELECT_SEQ_FMT = "SELECT value FROM tc_sequence WHERE id='%s'###";
    public static final String QRY_INIT_SEQ_FMT   = "INSERT INTO tc_sequence (id,value) VALUES ('%s', %d)###";
    public static final String QRY_UPDATE_SEQ     = "UPDATE tc_sequence SET value=? WHERE id=?###";

    public static final String QRY_INSERT_TC_MASTER_FMT =
            "INSERT INTO tc_master (TC_ID, tc_name, DESCRIPTION, created_by, CREATED_DATE, config_ids, ait_no, is_active) VALUES ('%s','%s','%s','system', CURRENT_TIMESTAMP, 'CFG_AUTO', '%s', 1)###";

    public static final String QRY_INSERT_TC_STEP_FMT =
            "INSERT INTO tc_steps (tc_id, step_name, PARAMETERS, SEQUENCE_NO, status) VALUES ('%s','%s','%s',%d,'PENDING')###";

    // ===================== MySQL artifact templates (single-line + ###) =====================
    public static final class MYSQL {
        public static final String HEADER_FMT =
                "-- MySQL artifact for AIT_NO(bare)=%s, DBTYPE=%s, DISC=%s START TRANSACTION;###";

        public static final String FOOTER = "COMMIT;###";

        // DIAC (ait_config)
        public static final String DIAC_FMT =
                "DELETE FROM ait_config WHERE AIT_NO = '%s';### INSERT INTO ait_config (AIT_NO, FUNNEL_SDD, AIML_SDD, IDW_SDD, IEDPS_SDD, ESPIAL_SDD, FUNNEL_FFT, IDW_FFT, ESPIAL_FFT, IS_ESPIAL, IS_ACTIVE, AIML_VALIDATION, FULL_SCAN, TOPIC_NAME, AIT_CADENCE, REPORT_TOPIC_NAME, FFT_DESTINATION, PROFILE, LOB, LAST_UPDATED_USER, LAST_UPDATED) VALUES ('%s', %d, %d, %d, %d, %d, %d, %d, %d, 0, 1, 0, 0, '%s', NULL, NULL, NULL, '%s', NULL, '%s', NOW());###";

        // DIDP (ait_dbprop)
        public static final String DIDP_FMT =
                "DELETE FROM ait_dbprop WHERE AIT_NO = '%s';### INSERT INTO ait_dbprop (AIT_NO, ID, DB_TYPE, MACHINE_NAME, DB_NAME, SCHEMA_NAME, USER_ID, PASS_WORD, TOPIC_NAME, JDBC_URL, IS_ACTIVE, LAST_UPDATED_USER, PROFILE, LAST_UPDATED) VALUES ('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s',1,'%s','%s',NOW());###";

        // DIKS (kafka_stat)
        public static final String DIKS_HEADER_FMT =
                "DELETE FROM kafka_stat WHERE AIT_NO = '%s';###";

        public static final String DIKS_ROW_FMT =
                "INSERT INTO kafka_stat (AIT_NO, START_TIME, EVENT, END_TIME, DURATION, SEQ_NO, STATUS, TOPIC_NAME, GROUP_ID, DB_TYPE, SCHEMA_NAME, MACHINE_NAME, PROFILE, CONFIG_ID, LAST_UPDATED_USER, SCHEDULERID, LAST_UPDATED) VALUES ('%s','%s','%s','%s',%d,%d,'%s','%s','%s','%s','%s','%s','%s','%s','%s','%s',NOW());###";
    }

    // ===================== SQL Server artifact templates (single-line + ###) =====================
    public static final class SQLSERVER {
        public static final String HEADER_FMT =
                "-- SQL Server artifact for AIT_NO(bare)=%s, DBTYPE=%s, DISC=%s BEGIN TRANSACTION;###";

        public static final String FOOTER = "COMMIT TRANSACTION;###";

        // DIAC (ait_config)
        public static final String DIAC_FMT =
                "DELETE FROM ait_config WHERE AIT_NO = '%s';### INSERT INTO ait_config (AIT_NO, FUNNEL_SDD, AIML_SDD, IDW_SDD, IEDPS_SDD, ESPIAL_SDD, FUNNEL_FFT, IDW_FFT, ESPIAL_FFT, IS_ESPIAL, IS_ACTIVE, AIML_VALIDATION, FULL_SCAN, TOPIC_NAME, AIT_CADENCE, REPORT_TOPIC_NAME, FFT_DESTINATION, PROFILE, LOB, LAST_UPDATED_USER, LAST_UPDATED) VALUES ('%s', %d, %d, %d, %d, %d, %d, %d, %d, 0, 1, 0, 0, '%s', NULL, NULL, NULL, '%s', NULL, '%s', GETDATE());###";

        // DIDP (ait_dbprop)
        public static final String DIDP_FMT =
                "DELETE FROM ait_dbprop WHERE AIT_NO = '%s';### INSERT INTO ait_dbprop (AIT_NO, ID, DB_TYPE, MACHINE_NAME, DB_NAME, SCHEMA_NAME, USER_ID, PASS_WORD, TOPIC_NAME, JDBC_URL, IS_ACTIVE, LAST_UPDATED_USER, PROFILE, LAST_UPDATED) VALUES ('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s',1,'%s','%s',GETDATE());###";

        // DIKS (kafka_stat)
        public static final String DIKS_HEADER_FMT =
                "DELETE FROM kafka_stat WHERE AIT_NO = '%s';###";

        public static final String DIKS_ROW_FMT =
                "INSERT INTO kafka_stat (AIT_NO, START_TIME, EVENT, END_TIME, DURATION, SEQ_NO, STATUS, TOPIC_NAME, GROUP_ID, DB_TYPE, SCHEMA_NAME, MACHINE_NAME, PROFILE, CONFIG_ID, LAST_UPDATED_USER, SCHEDULERID, LAST_UPDATED) VALUES ('%s','%s','%s','%s',%d,%d,'%s','%s','%s','%s','%s','%s','%s','%s','%s','%s',GETDATE());###";
    }
}
    // ===== DB queries for sequences, master, steps =====
    public static final String QRY_SELECT_SEQ_FMT   = "SELECT value FROM tc_sequence WHERE id='%s'";
    public static final String QRY_INIT_SEQ_FMT     = "INSERT INTO tc_sequence (id,value) VALUES ('%s', %d)";
    public static final String QRY_UPDATE_SEQ       = "UPDATE tc_sequence SET value=? WHERE id=?";

    public static final String QRY_INSERT_TC_MASTER_FMT =
            "INSERT INTO tc_master (TC_ID, tc_name, DESCRIPTION, created_by, CREATED_DATE, config_ids, ait_no, is_active) " +
                    "VALUES ('%s','%s','%s','system', CURRENT_TIMESTAMP, 'CFG_AUTO', '%s', 1)";

    public static final String QRY_INSERT_TC_STEP_FMT =
            "INSERT INTO tc_steps (tc_id, step_name, PARAMETERS, SEQUENCE_NO, status) VALUES ('%s','%s','%s',%d,'PENDING')";

    // ===================== MySQL artifact templates =====================
    public static final class MYSQL {
        public static final String HEADER_FMT =
                "-- MySQL artifact for AIT_NO(bare)=%s, DBTYPE=%s, DISC=%s\nSTART TRANSACTION;\n";

        public static final String FOOTER = "COMMIT;\n";

        // DIAC (ait_config)
        public static final String DIAC_FMT =
                """
                -- DIAC (ait_config)
                DELETE FROM ait_config WHERE AIT_NO = '%s';
                INSERT INTO ait_config (
                  AIT_NO, FUNNEL_SDD, AIML_SDD, IDW_SDD, IEDPS_SDD, ESPIAL_SDD,
                  FUNNEL_FFT, IDW_FFT, ESPIAL_FFT, IS_ESPIAL, IS_ACTIVE,
                  AIML_VALIDATION, FULL_SCAN,
                  TOPIC_NAME, AIT_CADENCE, REPORT_TOPIC_NAME, FFT_DESTINATION,
                  PROFILE, LOB, LAST_UPDATED_USER, LAST_UPDATED
                ) VALUES (
                  '%s', %d, %d, %d, %d, %d,
                  %d, %d, %d, 0, 1,
                  0, 0,
                  '%s', NULL, NULL, NULL,
                  '%s', NULL, '%s', NOW()
                );
                """;

        // DIDP (ait_dbprop) — keep only the columns you actually use
        public static final String DIDP_FMT =
                """
                -- DIDP (ait_dbprop)
                DELETE FROM ait_dbprop WHERE AIT_NO = '%s';
                INSERT INTO ait_dbprop (
                  AIT_NO, ID, DB_TYPE, MACHINE_NAME, DB_NAME, SCHEMA_NAME, USER_ID, PASS_WORD,
                  TOPIC_NAME, JDBC_URL, IS_ACTIVE, LAST_UPDATED_USER, PROFILE, LAST_UPDATED
                ) VALUES (
                  '%s','%s','%s','%s','%s','%s','%s','%s',
                  '%s','%s',1,'%s','%s',NOW()
                );
                """;

        // DIKS (kafka_stat)
        public static final String DIKS_HEADER_FMT =
                """
                -- DIKS (kafka_stat)
                -- cleanup for this AIT
                DELETE FROM kafka_stat WHERE AIT_NO = '%s';
                """;

        public static final String DIKS_ROW_FMT =
                """
                INSERT INTO kafka_stat (
                  AIT_NO, START_TIME, EVENT, END_TIME, DURATION, SEQ_NO,
                  STATUS, TOPIC_NAME, GROUP_ID, DB_TYPE, SCHEMA_NAME, MACHINE_NAME,
                  PROFILE, CONFIG_ID, LAST_UPDATED_USER, SCHEDULERID, LAST_UPDATED
                ) VALUES (
                  '%s','%s','%s','%s',%d,%d,
                  '%s','%s','%s','%s','%s','%s',
                  '%s','%s','%s','%s',NOW()
                );
                """;
    }

    // ===================== SQL Server artifact templates =====================
    public static final class SQLSERVER {
        public static final String HEADER_FMT =
                "-- SQL Server artifact for AIT_NO(bare)=%s, DBTYPE=%s, DISC=%s\nBEGIN TRANSACTION;\n";

        public static final String FOOTER = "COMMIT TRANSACTION;\n";

        // DIAC (ait_config)
        public static final String DIAC_FMT =
                """
                -- DIAC (ait_config)
                DELETE FROM ait_config WHERE AIT_NO = '%s';
                INSERT INTO ait_config (
                  AIT_NO, FUNNEL_SDD, AIML_SDD, IDW_SDD, IEDPS_SDD, ESPIAL_SDD,
                  FUNNEL_FFT, IDW_FFT, ESPIAL_FFT, IS_ESPIAL, IS_ACTIVE,
                  AIML_VALIDATION, FULL_SCAN,
                  TOPIC_NAME, AIT_CADENCE, REPORT_TOPIC_NAME, FFT_DESTINATION,
                  PROFILE, LOB, LAST_UPDATED_USER, LAST_UPDATED
                ) VALUES (
                  '%s', %d, %d, %d, %d, %d,
                  %d, %d, %d, 0, 1,
                  0, 0,
                  '%s', NULL, NULL, NULL,
                  '%s', NULL, '%s', GETDATE()
                );
                """;

        // DIDP (ait_dbprop)
        public static final String DIDP_FMT =
                """
                -- DIDP (ait_dbprop)
                DELETE FROM ait_dbprop WHERE AIT_NO = '%s';
                INSERT INTO ait_dbprop (
                  AIT_NO, ID, DB_TYPE, MACHINE_NAME, DB_NAME, SCHEMA_NAME, USER_ID, PASS_WORD,
                  TOPIC_NAME, JDBC_URL, IS_ACTIVE, LAST_UPDATED_USER, PROFILE, LAST_UPDATED
                ) VALUES (
                  '%s','%s','%s','%s','%s','%s','%s','%s',
                  '%s','%s',1,'%s','%s',GETDATE()
                );
                """;

        // DIKS (kafka_stat)
        public static final String DIKS_HEADER_FMT =
                """
                -- DIKS (kafka_stat)
                DELETE FROM kafka_stat WHERE AIT_NO = '%s';
                """;

        public static final String DIKS_ROW_FMT =
                """
                INSERT INTO kafka_stat (
                  AIT_NO, START_TIME, EVENT, END_TIME, DURATION, SEQ_NO,
                  STATUS, TOPIC_NAME, GROUP_ID, DB_TYPE, SCHEMA_NAME, MACHINE_NAME,
                  PROFILE, CONFIG_ID, LAST_UPDATED_USER, SCHEDULERID, LAST_UPDATED
                ) VALUES (
                  '%s','%s','%s','%s',%d,%d,
                  '%s','%s','%s','%s','%s','%s',
                  '%s','%s','%s','%s',GETDATE()
                );
                """;
    }
}
