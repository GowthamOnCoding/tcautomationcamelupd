package com.boa.tcautomation.service;

import com.boa.tcautomation.helper.TcMasterServiceHelper;
import com.boa.tcautomation.json.model.ExportToCSVJSON;
import com.boa.tcautomation.json.model.LinuxCommandJSON;
import com.boa.tcautomation.json.model.ResetDbpropAndEnableToolsJSON;
import com.boa.tcautomation.json.model.ScanWindowDeleteInsertJSON;
import com.boa.tcautomation.model.AitDbProp;
import com.boa.tcautomation.model.TcMaster;
import com.boa.tcautomation.model.TcSteps;
import com.boa.tcautomation.util.DatabaseToCsvUtil;
import com.boa.tcautomation.util.DbUtil;
import com.boa.tcautomation.util.SshUtil;
import com.boa.tcautomation.validator.ParameterValidationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;

@Service
public class TcExecutionService {

    @Autowired
    private DbUtil dbUtil;

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
        try {
            if (tcMasterServiceHelper.getAndValidateParametersSchema(tcStep)) {
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
            } else {
                throw new RuntimeException("Failed to get and validate StepConfig");
            }
        } catch (Exception e) {
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
        try {
            if (tcMasterServiceHelper.getAndValidateParametersSchema(step)) {
                LinuxCommandJSON linuxCommandJSON = new ObjectMapper().readValue(step.getParameters(), LinuxCommandJSON.class);
                String output = sshUtil.executeCommand(linuxCommandJSON.getHostname(), linuxCommandJSON.getCommand());
            } else {
                throw new RuntimeException("Failed to get and validate StepConfig");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error running command: " + e.getMessage(), e);
        }
    }

    public void exportTableToCsv(TcMaster tcMaster, TcSteps step) {
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
            } else {
                throw new RuntimeException("Schema validation failed");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error running command: " + e.getMessage(), e);
        }
    }

    public void resetAndEnableTools(TcMaster tcMaster, TcSteps tcStep) {
        try {
            if (tcMasterServiceHelper.getAndValidateParametersSchema(tcStep)) {
                ResetDbpropAndEnableToolsJSON resetDbpropAndEnableToolsJSON = new ObjectMapper().readValue(tcStep.getParameters(), ResetDbpropAndEnableToolsJSON.class);
                String resetDbpropQuery = tcMasterServiceHelper.buildDBPropResetQuery(tcMaster, resetDbpropAndEnableToolsJSON);
                boolean resetSuccess = dbUtil.executeQuery(resetDbpropQuery);
                String enableToolsQuery = tcMasterServiceHelper.buildEnableToolsQuery(tcMaster, resetDbpropAndEnableToolsJSON);
                boolean enableSuccess = dbUtil.executeQuery(enableToolsQuery);

            } else {
                throw new RuntimeException("Schema validation failed");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error running command: " + e.getMessage(), e);
        }
    }
}
