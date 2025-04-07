package com.boa.tcautomation.service;

import com.boa.tcautomation.model.ParameterSchema;
import com.boa.tcautomation.model.StepConfig;
import com.boa.tcautomation.model.TcMaster;
import com.boa.tcautomation.model.TcSteps;
import com.boa.tcautomation.repository.ParameterSchemaRepository;
import com.boa.tcautomation.repository.StepConfigRepository;
import com.boa.tcautomation.repository.TcMasterRepository;
import com.boa.tcautomation.repository.TcStepsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TestAPIService {
    private static final Logger logger = LoggerFactory.getLogger(TcMasterService.class);
    @Autowired
    private TcStepsRepository tcStepsRepository;
    @Autowired
    private TcMasterRepository tcMasterRepository;
    @Autowired
    private StepConfigRepository stepConfigRepository;
    @Autowired
    private ParameterSchemaRepository parameterSchemaRepository;
    public List<TcMaster> getAllTestCases() {
        logger.info("Entering findAll method");
        List<TcMaster> result = tcMasterRepository.findAll();
        logger.info("Exiting findAll method with result: {}", result);
        return result;
    }

    public ParameterSchema getParametersByTestCaseId(String testcaseId) {
        Optional<ParameterSchema> paramSchema = parameterSchemaRepository.findById(testcaseId);
        return paramSchema.orElse(null);
    }

    public List<TcSteps> getTestStepsByTestCaseId(String testcaseId) {
            logger.info("Entering findAll method");
            List<TcSteps> result = tcStepsRepository.findByTcId(testcaseId);
            logger.info("Exiting findAll method with result: {}", result);
            return result;
    }
   public List<StepConfig> getAllStepConfigs() {
        logger.info("Entering findAll method");
        List<StepConfig> result = stepConfigRepository.findAll();
        logger.info("Exiting findAll method with result: {}", result);
        return result;
    }
}
