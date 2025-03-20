package com.boa.tcautomation.service;

import com.boa.tcautomation.helper.TcMasterServiceHelper;
import com.boa.tcautomation.json.model.*;
import com.boa.tcautomation.model.AitDbProp;
import com.boa.tcautomation.model.TcMaster;
import com.boa.tcautomation.model.TcSteps;
import com.boa.tcautomation.util.Constants;
import com.boa.tcautomation.util.DatabaseToCsvUtil;
import com.boa.tcautomation.util.DbUtil;
import com.boa.tcautomation.util.SshUtil;
import com.boa.tcautomation.validator.ParameterValidationService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

@Service
public class TcExecutionService {

    @Autowired
    private DbUtil dbUtil;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private SshUtil sshUtil;

    @Autowired
    private DatabaseToCsvUtil databaseToCsvUtil;

    @Autowired
    private ParameterValidationService parameterValidationService;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private TcMasterServiceHelper tcMasterServiceHelper;

    @Autowired
    private Environment env;
    private static final Logger log = LoggerFactory.getLogger(TcExecutionService.class);

    public void deleteInsertScanWindow(TcMaster tcMaster, TcSteps tcStep) {
        log.info("{}#{} updating TC_Execution log to In Progress", tcMaster.getTcId(), tcStep.getStepId());
        long tcExecId = tcMasterServiceHelper.insertLogEntry(tcMaster.getTcId(), tcStep.getStepId(), Constants.INPROGRESS);
        log.info("{}#{} deleting and inserting scan window", tcMaster.getTcId(), tcStep.getStepId());

        try {
            if (tcMasterServiceHelper.getAndValidateParametersSchema(tcStep)) {
                log.info("Parameters -> {}", tcStep.getParameters());
                ScanWindowDeleteInsertJSON scanWindowDeleteInsertJSON = new ObjectMapper().readValue(tcStep.getParameters(), ScanWindowDeleteInsertJSON.class);
                String aitNo = tcMaster.getAitNo();
                if (aitNo.startsWith("AIT_")) {
                    aitNo = aitNo.replace("AIT_", "");
                }
                List<AitDbProp> aitDbProps = tcMasterServiceHelper.queryAitDbProps(aitNo);
                for (AitDbProp aitDbProp : aitDbProps) {
                    boolean deleteSuccess = tcMasterServiceHelper.deleteAitScanWindow("AIT_" + aitDbProp.getAitNo(), aitDbProp.getDbType());
                    boolean insertSuccess = tcMasterServiceHelper.insertAitScanWindow(aitDbProp);
                }
                log.info("{}#{} updating TC_Execution log to Completed", tcMaster.getTcId(), tcStep.getStepId());
                tcMasterServiceHelper.updateLogEntry(tcExecId, Constants.COMPLETED, "");
            } else {
                log.info("{}#{} updating TC_Execution log to Failed", tcMaster.getTcId(), tcStep.getStepId());
                tcMasterServiceHelper.updateLogEntry(tcExecId, Constants.FAILED, "");
                throw new RuntimeException("Schema validation failed");
            }
        } catch (Exception e) {
            log.info("{}#{} updating TC_Execution log to Failed", tcMaster.getTcId(), tcStep.getStepId());
            tcMasterServiceHelper.updateLogEntry(tcExecId, Constants.FAILED, "");
            throw new RuntimeException("Error running command: " + e.getMessage(), e);
        }
    }

    public void processTestCase(TcMaster tcMaster) {
        List<TcSteps> tcSteps = tcMasterServiceHelper.getTcStepsByTcId(tcMaster.getTcId());
        for (TcSteps step : tcSteps) {
            String methodName = step.getStepName();
            try {
                Method method = this.getClass().getMethod(methodName, TcMaster.class, TcSteps.class);
                TcExecutionService proxy = applicationContext.getBean(TcExecutionService.class);
                method.invoke(proxy, tcMaster, step);
            } catch (Exception e) {
                throw new RuntimeException("Error invoking method " + methodName + ": " + e.getMessage(), e);
            }
        }
    }

    public void runLinuxCommand(TcMaster tcMaster, TcSteps step) {
        log.info("{}#{} updating TC_Execution log to In Progress", tcMaster.getTcId(), step.getStepId());
        long tcExecId = tcMasterServiceHelper.insertLogEntry(tcMaster.getTcId(), step.getStepId(), Constants.INPROGRESS);
        log.info("{}#{} running Linux command", tcMaster.getTcId(), step.getStepId());

        try {
            if (tcMasterServiceHelper.getAndValidateParametersSchema(step)) {
                LinuxCommandJSON linuxCommandJSON = new ObjectMapper().readValue(step.getParameters(), LinuxCommandJSON.class);
                String output = sshUtil.executeCommand(linuxCommandJSON.getHostname(), linuxCommandJSON.getCommand());
                log.info("{}#{} updating TC_Execution log to Completed", tcMaster.getTcId(), step.getStepId());
                tcMasterServiceHelper.updateLogEntry(tcExecId, Constants.COMPLETED, "");
            } else {
                log.info("{}#{} updating TC_Execution log to Failed", tcMaster.getTcId(), step.getStepId());
                tcMasterServiceHelper.updateLogEntry(tcExecId, Constants.FAILED, "");
                throw new RuntimeException("Schema validation failed");
            }
        } catch (Exception e) {
            log.info("{}#{} updating TC_Execution log to Failed", tcMaster.getTcId(), step.getStepId());
            tcMasterServiceHelper.updateLogEntry(tcExecId, Constants.FAILED, "");
            throw new RuntimeException("Error running command: " + e.getMessage(), e);
        }
    }

    public void exportTableToCsv(TcMaster tcMaster, TcSteps step) {
        log.info("{}#{} updating TC_Execution log to In Progress", tcMaster.getTcId(), step.getStepId());
        long tcExecId = tcMasterServiceHelper.insertLogEntry(tcMaster.getTcId(), step.getStepId(), Constants.INPROGRESS);
        log.info("{}#{} exporting table to CSV", tcMaster.getTcId(), step.getStepId());

        try {
            if (tcMasterServiceHelper.getAndValidateParametersSchema(step)) {
                ExportToCSVJSON exportToCSVJSON = tcMasterServiceHelper.parseExportToCSVJSON(step);
                String exportQuery = tcMasterServiceHelper.buildExportQuery(tcMaster, exportToCSVJSON);
                log.info("Export Query: " + exportQuery);
                String destination = tcMasterServiceHelper.createDestinationDirectory(tcMaster);
                String fileName = tcMasterServiceHelper.generateFileName(step, exportToCSVJSON, tcMaster);
                log.info("Exporting to: " + destination + File.separator + fileName);
                tcMasterServiceHelper.handleExistingFile(destination, fileName);
                databaseToCsvUtil.queryToCsv(exportQuery, destination, fileName);
                log.info("{}#{} updating TC_Execution log to Completed", tcMaster.getTcId(), step.getStepId());
                tcMasterServiceHelper.updateLogEntry(tcExecId, Constants.COMPLETED, "");
            } else {
                log.info("{}#{} updating TC_Execution log to Failed", tcMaster.getTcId(), step.getStepId());
                tcMasterServiceHelper.updateLogEntry(tcExecId, Constants.FAILED, "");
                throw new RuntimeException("Schema validation failed");
            }
        } catch (Exception e) {
            log.info("{}#{} updating TC_Execution log to Failed", tcMaster.getTcId(), step.getStepId());
            tcMasterServiceHelper.updateLogEntry(tcExecId, Constants.FAILED, "");
            throw new RuntimeException("Error running command: " + e.getMessage(), e);
        }
    }

    public void resetAndEnableTools(TcMaster tcMaster, TcSteps tcStep) {
        log.info("{}#{} updating TC_Execution log to In Progress", tcMaster.getTcId(), tcStep.getStepId());
        long tcExecId = tcMasterServiceHelper.insertLogEntry(tcMaster.getTcId(), tcStep.getStepId(), Constants.INPROGRESS);
        log.info("{}#{} resetting DB properties and enabling tools", tcMaster.getTcId(), tcStep.getStepId());

        try {
            if (tcMasterServiceHelper.getAndValidateParametersSchema(tcStep)) {
                ResetDbpropAndEnableToolsJSON resetDbpropAndEnableToolsJSON = new ObjectMapper().readValue(tcStep.getParameters(), ResetDbpropAndEnableToolsJSON.class);
                String resetDbpropQuery = tcMasterServiceHelper.buildDBPropResetQuery(tcMaster, resetDbpropAndEnableToolsJSON);
                boolean resetSuccess = dbUtil.executeQuery(resetDbpropQuery);
                String enableToolsQuery = tcMasterServiceHelper.buildEnableToolsQuery(tcMaster, resetDbpropAndEnableToolsJSON);
                boolean enableSuccess = dbUtil.executeQuery(enableToolsQuery);
                log.info("{}#{} updating TC_Execution log to Completed", tcMaster.getTcId(), tcStep.getStepId());
                tcMasterServiceHelper.updateLogEntry(tcExecId, Constants.COMPLETED, "");
            } else {
                log.info("{}#{} updating TC_Execution log to Failed", tcMaster.getTcId(), tcStep.getStepId());
                tcMasterServiceHelper.updateLogEntry(tcExecId, Constants.FAILED, "");
                throw new RuntimeException("Schema validation failed");
            }
        } catch (Exception e) {
            log.info("{}#{} updating TC_Execution log to Failed", tcMaster.getTcId(), tcStep.getStepId());
            tcMasterServiceHelper.updateLogEntry(tcExecId, Constants.FAILED, "");
            throw new RuntimeException("Error running command: " + e.getMessage(), e);
        }
    }
    public void insertKafkaStatEntriesWithJDBCTemplate(TcMaster tcMaster, TcSteps tcStep) {
        log.info("{}#{} updating TC_Execution log to In Progress", tcMaster.getTcId(), tcStep.getStepId());
        long tcExecId = tcMasterServiceHelper.insertLogEntry(tcMaster.getTcId(), tcStep.getStepId(), Constants.INPROGRESS);
        log.info("{}#{} inserting entries into Kafka Stat Table", tcMaster.getTcId(), tcStep.getStepId());

        try {
            if (tcMasterServiceHelper.getAndValidateParametersSchema(tcStep)) {
                // Parse the parameters
                ObjectMapper objectMapper = new ObjectMapper();
                List<KafkaStatEntry> kafkaStatEntries = objectMapper.readValue(tcStep.getParameters(), new TypeReference<List<KafkaStatEntry>>() {});

                for (KafkaStatEntry entry : kafkaStatEntries) {
                    // Validate ait_number and config_id combination
                    String aitNo = tcMaster.getAitNo().replaceFirst("^(SDD_AIT_|AIT_)", "");
                    String validateQuery = "SELECT COUNT(*) FROM ait_dbprop WHERE ait_no = ? AND config_id = ?";
                    Integer count = jdbcTemplate.queryForObject(validateQuery, new Object[]{aitNo, entry.getConfigId()}, Integer.class);
                    if (count == null || count == 0) {
                        throw new RuntimeException("Invalid ait_number and config_id combination");
                    }

                    // Fetch seqNo from AIT_SEQ table
                    String seqNoQuery = "SELECT seq_no FROM ait_seq WHERE ait_no = ? AND config_id = ?";
                    Integer seqNo = jdbcTemplate.queryForObject(seqNoQuery, new Object[]{aitNo, entry.getConfigId()}, Integer.class);
                    if (seqNo == null) {
                        throw new RuntimeException("SeqNo not found for the given ait_number and config_id");
                    }

                    // Fetch groupid, dbtype, schemaname, and profile from ait_dbprop table
                    String aitDbPropQuery = "SELECT group_id, db_type, schema_name, profile FROM ait_dbprop WHERE ait_no = ? AND config_id = ?";
                    Map<String, Object> aitDbProp = jdbcTemplate.queryForMap(aitDbPropQuery, aitNo, entry.getConfigId());

                    // Build the insert query
                    String insertQuery = "INSERT INTO kafka_stat (ait_no, start_time, last_updated, status, seq_no, group_id, db_type, schema_name, profile) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    jdbcTemplate.update(insertQuery, aitNo, entry.getStartTime(), entry.getLastUpdated(),
                            entry.getStatus(), seqNo, aitDbProp.get("group_id"), aitDbProp.get("db_type"),
                            aitDbProp.get("schema_name"), aitDbProp.get("profile"));
                }

                log.info("{}#{} updating TC_Execution log to Completed", tcMaster.getTcId(), tcStep.getStepId());
                tcMasterServiceHelper.updateLogEntry(tcExecId, Constants.COMPLETED, "");
            } else {
                log.info("{}#{} updating TC_Execution log to Failed", tcMaster.getTcId(), tcStep.getStepId());
                tcMasterServiceHelper.updateLogEntry(tcExecId, Constants.FAILED, "");
                throw new RuntimeException("Schema validation failed");
            }
        } catch (Exception e) {
            log.info("{}#{} updating TC_Execution log to Failed", tcMaster.getTcId(), tcStep.getStepId());
            tcMasterServiceHelper.updateLogEntry(tcExecId, Constants.FAILED, "");
            throw new RuntimeException("Error inserting Kafka Stat entries: " + e.getMessage(), e);
        }
    }
/*
[{"event":"metadata","status":"Completed","startTime":"2023-10-01T10:00:00","lastUpdated":"2023-10-01T10:15:00","aitNo":"SDD_AIT_12345","configId":"config_001"},{"event":"producer","status":"Partially Processed","startTime":"2023-10-01T10:00:00","lastUpdated":"2023-10-01T10:15:00","aitNo":"AIT_67890","configId":"config_002"},{"event":"consumer","status":"Completed","startTime":"2023-10-01T10:00:00","lastUpdated":"2023-10-01T10:15:00","aitNo":"AIT_54321","configId":"config_003"}]
{"$schema":"http://json-schema.org/draft-07/schema#","type":"array","items":{"type":"object","properties":{"event":{"type":"string"},"status":{"type":"string"},"startTime":{"type":"string","format":"date-time"},"lastUpdated":{"type":"string","format":"date-time"},"aitNo":{"type":"string"},"configId":{"type":"string"}},"required":["event","status","startTime","lastUpdated","aitNo","configId"]}}
 */
    public void insertKafkaStatEntries(TcMaster tcMaster, TcSteps tcStep) {
        log.info("{}#{} updating TC_Execution log to In Progress", tcMaster.getTcId(), tcStep.getStepId());
        long tcExecId = tcMasterServiceHelper.insertLogEntry(tcMaster.getTcId(), tcStep.getStepId(), Constants.INPROGRESS);
        log.info("{}#{} inserting entries into Kafka Stat Table", tcMaster.getTcId(), tcStep.getStepId());

        try {
            if (tcMasterServiceHelper.getAndValidateParametersSchema(tcStep)) {
                // Parse the parameters
                ObjectMapper objectMapper = new ObjectMapper();
                List<KafkaStatEntry> kafkaStatEntries = objectMapper.readValue(tcStep.getParameters(), new TypeReference<List<KafkaStatEntry>>() {});

                for (KafkaStatEntry entry : kafkaStatEntries) {
                    insertKafkaStatEntry(entry, tcMaster.getAitNo());
                }

                log.info("{}#{} updating TC_Execution log to Completed", tcMaster.getTcId(), tcStep.getStepId());
                tcMasterServiceHelper.updateLogEntry(tcExecId, Constants.COMPLETED, "");
            } else {
                log.info("{}#{} updating TC_Execution log to Failed", tcMaster.getTcId(), tcStep.getStepId());
                tcMasterServiceHelper.updateLogEntry(tcExecId, Constants.FAILED, "");
                throw new RuntimeException("Schema validation failed");
            }
        } catch (Exception e) {
            log.info("{}#{} updating TC_Execution log to Failed", tcMaster.getTcId(), tcStep.getStepId());
            tcMasterServiceHelper.updateLogEntry(tcExecId, Constants.FAILED, "");
            throw new RuntimeException("Error inserting Kafka Stat entries: " + e.getMessage(), e);
        }
    }
    private void insertKafkaStatEntry(KafkaStatEntry entry, String aitNo) {
        // Validate ait_number and config_id combination
        aitNo = aitNo.replaceFirst("^(SDD_AIT_|AIT_)", "");
        String validateQuery = "SELECT COUNT(*) FROM ait_dbprop WHERE ait_no = :aitNo AND config_id = :configId";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("aitNo", aitNo)
                .addValue("configId", entry.getConfigId());
        Integer count = namedParameterJdbcTemplate.queryForObject(validateQuery, params, Integer.class);
        if (count == null || count == 0) {
            throw new RuntimeException("Invalid ait_number and config_id combination");
        }

        // Fetch seqNo from AIT_SEQ table
        String seqNoQuery = "SELECT seq_no FROM ait_seq WHERE ait_no = :aitNo AND config_id = :configId";
        Integer seqNo = namedParameterJdbcTemplate.queryForObject(seqNoQuery, params, Integer.class);
        if (seqNo == null) {
            throw new RuntimeException("SeqNo not found for the given ait_number and config_id");
        }

        // Fetch groupid, dbtype, schemaname, and profile from ait_dbprop table
        String aitDbPropQuery = "SELECT group_id, db_type, schema_name, profile FROM ait_dbprop WHERE ait_no = :aitNo AND config_id = :configId";
        Map<String, Object> aitDbProp = namedParameterJdbcTemplate.queryForMap(aitDbPropQuery, params);

        // Build the insert query with default values
        String insertQuery = "INSERT INTO kafka_stat (ait_no, start_time, last_updated, status, seq_no, group_id, db_type, schema_name, profile, " +
                "event, process_no, table_name, total_rows, total_fft_instances, total_messages, end_time, duration, remarks, topic_name, " +
                "machine_name, last_updated_user, scheduler_id) " +
                "VALUES (:aitNo, :startTime, :lastUpdated, :status, :seqNo, :groupId, :dbType, :schemaName, :profile, " +
                ":event, :processNo, :tableName, :totalRows, :totalFftInstances, :totalMessages, :endTime, :duration, :remarks, :topicName, " +
                ":machineName, :lastUpdatedUser, :schedulerId)";
        MapSqlParameterSource insertParams = new MapSqlParameterSource()
                .addValue("aitNo", aitNo)
                .addValue("startTime", entry.getStartTime())
                .addValue("lastUpdated", entry.getLastUpdated())
                .addValue("status", entry.getStatus())
                .addValue("seqNo", seqNo)
                .addValue("groupId", aitDbProp.get("group_id"))
                .addValue("dbType", aitDbProp.get("db_type"))
                .addValue("schemaName", aitDbProp.get("schema_name"))
                .addValue("profile", aitDbProp.get("profile"))
                .addValue("event", entry.getEvent() != null ? entry.getEvent() : "default_event")
                .addValue("processNo", "default_process_no")
                .addValue("tableName", "default_table_name")
                .addValue("totalRows", 0L)
                .addValue("totalFftInstances", 0L)
                .addValue("totalMessages", 0)
                .addValue("endTime", null)
                .addValue("duration", 0.0)
                .addValue("remarks", "default_remarks")
                .addValue("topicName", "default_topic_name")
                .addValue("machineName", "default_machine_name")
                .addValue("lastUpdatedUser", "default_user")
                .addValue("schedulerId", "default_scheduler_id");
        namedParameterJdbcTemplate.update(insertQuery, insertParams);
    }
}
