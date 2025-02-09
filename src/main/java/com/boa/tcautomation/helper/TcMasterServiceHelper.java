package com.boa.tcautomation.helper;

import com.boa.tcautomation.json.model.ExportToCSVJSON;
import com.boa.tcautomation.json.model.ResetDbpropAndEnableToolsJSON;
import com.boa.tcautomation.model.*;
import com.boa.tcautomation.util.DbUtil;
import com.boa.tcautomation.util.QueryConstants;
import com.boa.tcautomation.validator.ParameterValidationService;
import static com.boa.tcautomation.util.QueryConstants.DELETE_KAFKA_STAT;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TcMasterServiceHelper {

    @Autowired
    private DbUtil dbUtil;
    @Autowired
    private Environment env;
    @Autowired
    private ParameterValidationService parameterValidationService;

    private static final Logger log = LoggerFactory.getLogger(TcMasterServiceHelper.class);

    private <T> T executeQuery(String sql, Class<T> clazz) {
        log.info("Executing SQL: {}", sql);
        return dbUtil.queryForObject(sql, clazz);
    }

    private <T> List<T> executeQueryForList(String sql, Class<T> clazz) {
        log.info("Executing SQL: {}", sql);
        return dbUtil.queryForListWithMapping(sql, clazz);
    }

    public List<AitDbProp> queryAitDbProps(String aitNo) {
        String sql = QueryConstants.SELECT_AIT_DB_PROPS.replace("<AIT_NO>", aitNo);
        return executeQueryForList(sql, AitDbProp.class);
    }

    public boolean deleteAitScanWindow(String aitNo, String dbType) {
        String deleteSql = QueryConstants.DELETE_AIT_SCAN_WINDOW
                .replace("<AIT_NO>", aitNo)
                .replace("<DB_TYPE>", dbType);
        return dbUtil.executeQuery(deleteSql);
    }

    public boolean insertAitScanWindow(AitDbProp aitDbProp) {
        String insertSql = QueryConstants.INSERT_AIT_SCAN_WINDOW
                .replace("<AIT_NO>", "AIT_" + aitDbProp.getAitNo())
                .replace("<DB_TYPE>", aitDbProp.getDbType())
                .replace("<PROFILE>", aitDbProp.getProfile());
        return dbUtil.executeQuery(insertSql);
    }

    public List<TcSteps> getTcStepsByTcId(String tcId) {
        String sql = QueryConstants.SELECT_TC_STEPS_BY_TC_ID.replace("<TC_ID>", tcId);
        return executeQueryForList(sql, TcSteps.class);
    }

    public ParameterSchema getParameterSchema(String schemaId) {
        String sql = QueryConstants.SELECT_PARAMETER_SCHEMA.replace("<SCHEMA_ID>", schemaId);
        return executeQuery(sql, ParameterSchema.class);
    }

    public boolean getAndValidateParametersSchema(TcSteps step) {
        try {
            String parameters = step.getParameters();
            log.info("Parameters: {}", parameters);
            String sql = QueryConstants.SELECT_STEP_CONFIG_BY_STEP_NAME.replace("<STEP_NAME>", step.getStepName());
            StepConfig stepConfig = executeQuery(sql, StepConfig.class);
            log.info("StepConfig: {}", stepConfig);
            if (stepConfig.getParameterSchema() != null && !stepConfig.getParameterSchema().isEmpty()) {
                ParameterSchema parameterSchema = getParameterSchema(stepConfig.getParameterSchema());
                Map<String, String> parametersMap = new ObjectMapper().readValue(parameters, Map.class);
                parameterValidationService.validateParameters(step.getStepName(), parametersMap);
            }
            return true;
        } catch (Exception e) {
            log.error("Error getting and validating StepConfig: {}", e.getMessage());
            return false;
        }
    }

    private String getAitNo(TcMaster tcMaster, String paramAitNo) {
        return validateAndGetAitNo(tcMaster, paramAitNo);
    }

    private static String validateAndGetAitNo(TcMaster tcMaster, String aitNo) {
        if (aitNo == null || aitNo.isEmpty()) {
            aitNo = tcMaster.getAitNo();
        }
        return aitNo;
    }

    private String getConfigIdStr(TcMaster tcMaster, ResetDbpropAndEnableToolsJSON resetDbpropAndEnableToolsJSON) {
        String configId = resetDbpropAndEnableToolsJSON.getConfigId();
        configId = validateAndGetCOnfigIds(tcMaster, configId);
        return getConfigIdFinalString(configId);
    }

    private static String getConfigIdFinalString(String configId) {
        List<String> configIdList = Arrays.asList(configId.split(","));
        return configIdList.stream()
                .map(id -> "'" + id + "'")
                .collect(Collectors.joining(","));
    }

    private static String validateAndGetCOnfigIds(TcMaster tcMaster, String configId) {
        if (configId == null || configId.isEmpty()) {
            configId = tcMaster.getConfigIds();
        }
        return configId;
    }

    public String buildDBPropResetQuery(TcMaster tcMaster, ResetDbpropAndEnableToolsJSON resetDbpropAndEnableToolsJSON) {
        String aitNo = getAitNo(tcMaster, resetDbpropAndEnableToolsJSON.getAitNumber());
        String configIdStr = getConfigIdStr(tcMaster, resetDbpropAndEnableToolsJSON);
        return QueryConstants.RESET_DBPROP_QUERY
                .replace("<AIT_NO>", aitNo)
                .replace("<CONFIG_ID>", configIdStr);
    }

    public String buildEnableToolsQuery(TcMaster tcMaster, ResetDbpropAndEnableToolsJSON resetDbpropAndEnableToolsJSON) {
        String aitNo = getAitNo(tcMaster, resetDbpropAndEnableToolsJSON.getAitNumber());
        String configIdStr = getConfigIdStr(tcMaster, resetDbpropAndEnableToolsJSON);

        boolean enableFunnel = resetDbpropAndEnableToolsJSON.getEnableTools().contains("funnel");
        boolean enableIdw = resetDbpropAndEnableToolsJSON.getEnableTools().contains("idw");
        boolean enableIedps = resetDbpropAndEnableToolsJSON.getEnableTools().contains("iedps");
        boolean enableAiml = resetDbpropAndEnableToolsJSON.getEnableTools().contains("aiml");

        return QueryConstants.ENABLE_TOOLS_QUERY
                .replace("<AIT_NO>", aitNo)
                .replace("<CONFIG_ID>", configIdStr)
                .replace("<ENABLE_FUNNEL>", String.valueOf(enableFunnel))
                .replace("<ENABLE_IDW>", String.valueOf(enableIdw))
                .replace("<ENABLE_IEDPS>", String.valueOf(enableIedps))
                .replace("<ENABLE_AIML>", String.valueOf(enableAiml));
    }

    public boolean deleteKafkaStatTable(String aitNumber) {
        String sql = DELETE_KAFKA_STAT;
        try {
            int rowsAffected = dbUtil.update(sql, aitNumber);
            return rowsAffected > 0;
        } catch (Exception e) {
            log.error("Error deleting KAFKA_STAT table for AIT_NUMBER {}: {}", aitNumber, e.getMessage());
            return false;
        }
    }

   public String buildExportQuery(TcMaster tcMaster, ExportToCSVJSON exportConfigId) {
    String aitNo = validateAndGetAitNo(tcMaster, exportConfigId.getAitNumber());
    String configId = validateAndGetCOnfigIds(tcMaster, exportConfigId.getConfigId());
    String configIdList = getConfigIdFinalString(configId);

    if ("AIT_DBPROP".equals(exportConfigId.getTableName())) {
        aitNo = aitNo.replaceFirst("^(AIT_|SDD_AIT_)", "");
        return QueryConstants.SELECT_QUERY_TEMPLATE
                .replace("<TABLE_NAME>", exportConfigId.getTableName())
                .replace("<AIT_NO>", aitNo)
                .replace("<CONFIG_ID>", configIdList)
                .replace("CONFIG_ID", "ID");

    }

    return QueryConstants.SELECT_QUERY_TEMPLATE
            .replace("<TABLE_NAME>", exportConfigId.getTableName())
            .replace("<AIT_NO>", aitNo)
            .replace("<CONFIG_ID>", configIdList);
    }

    public ExportToCSVJSON parseExportToCSVJSON(TcSteps step) throws IOException {
        return new ObjectMapper().readValue(step.getParameters(), ExportToCSVJSON.class);
    }

    public String createDestinationDirectory(TcMaster tcMaster) {
        String commonDestination = env.getProperty("common.destination");
        String destination = commonDestination + File.separator + tcMaster.getTcId();
        File directory = new File(destination);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        return destination;
    }

    public String generateFileName(TcSteps step, ExportToCSVJSON exportToCSVJSON, TcMaster tcMaster) {
        String tableName = exportToCSVJSON.getTableName();
        String aitNo = tcMaster.getAitNo();
        return step.getSequenceNo() + "_" + tableName + "_" + aitNo + ".csv";
    }

    public void handleExistingFile(String destination, String fileName) {
        File file = new File(destination, fileName);
        if (file.exists()) {
            String archivePath = destination + File.separator + "archive";
            File archiveDirectory = new File(archivePath);
            if (!archiveDirectory.exists()) {
                archiveDirectory.mkdirs();
            }
            File archiveFile = new File(archivePath, fileName);
            file.renameTo(archiveFile);
        }
    }
}
