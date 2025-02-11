package com.boa.tcautomation.controller;

import com.boa.tcautomation.model.StepConfig;
import com.boa.tcautomation.model.TcMaster;
import com.boa.tcautomation.model.TcSteps;
import com.boa.tcautomation.repository.StepConfigRepository;
import com.boa.tcautomation.service.TestAPIService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class APIContrOoller {
    @Autowired
    private TestAPIService testManagementService;

    private static final Logger logger = LoggerFactory.getLogger(APIContrOoller.class);

    @Autowired
    private StepConfigRepository stepConfigRepository;

    @GetMapping("/testcases")
    public List<TcMaster> getTestCases() {
        return testManagementService.getAllTestCases();
    }

    // Endpoint to fetch Test Steps by Test Case ID
    @GetMapping("/teststeps")
    public List<TcSteps> getTestSteps(@RequestParam String testcaseId) {
        return testManagementService.getTestStepsByTestCaseId(testcaseId);
    }
    @GetMapping("/stepnames")
    public List<StepConfig> listStepConfigs() {
       return testManagementService.getAllStepConfigs();
    }
}
