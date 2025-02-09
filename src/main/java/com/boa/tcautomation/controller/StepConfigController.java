package com.boa.tcautomation.controller;

import com.boa.tcautomation.model.StepConfig;
import com.boa.tcautomation.repository.StepConfigRepository;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.PostConstruct;

@Controller
@RequestMapping("/stepconfig")
public class StepConfigController {

    private static final Logger logger = LoggerFactory.getLogger(StepConfigController.class);

    @Autowired
    private StepConfigRepository stepConfigRepository;

    @PostConstruct
    public void init() {
        if (stepConfigRepository.count() == 0) {
            logger.info("Initializing sample step configurations...");
            
            // Create sample step 1
            StepConfig step1 = new StepConfig();
            step1.setStepName("REST_API_CALL");
            step1.setDescription("Makes a REST API call to the specified endpoint");
            step1.setTimeoutSeconds(30);
            step1.setMaxRetries(3);
            step1.setParameterSchema("{\n" +
                "  \"type\": \"object\",\n" +
                "  \"properties\": {\n" +
                "    \"url\": {\"type\": \"string\"},\n" +
                "    \"method\": {\"type\": \"string\", \"enum\": [\"GET\", \"POST\", \"PUT\", \"DELETE\"]},\n" +
                "    \"headers\": {\"type\": \"object\"},\n" +
                "    \"body\": {\"type\": \"object\"}\n" +
                "  },\n" +
                "  \"required\": [\"url\", \"method\"]\n" +
                "}");
            stepConfigRepository.save(step1);

            // Create sample step 2
            StepConfig step2 = new StepConfig();
            step2.setStepName("DB_QUERY");
            step2.setDescription("Executes a database query and validates the result");
            step2.setTimeoutSeconds(60);
            step2.setMaxRetries(2);
            step2.setParameterSchema("{\n" +
                "  \"type\": \"object\",\n" +
                "  \"properties\": {\n" +
                "    \"query\": {\"type\": \"string\"},\n" +
                "    \"expectedRows\": {\"type\": \"integer\"},\n" +
                "    \"connectionString\": {\"type\": \"string\"}\n" +
                "  },\n" +
                "  \"required\": [\"query\", \"connectionString\"]\n" +
                "}");
            stepConfigRepository.save(step2);

            logger.info("Sample step configurations initialized successfully");
        }
    }

    @GetMapping({"/list", "/stepconfigs"})
    public String listStepConfigs(Model model) {
        logger.info("Fetching all step configurations");
        List<StepConfig> stepconfigs = stepConfigRepository.findAll();
        logger.info("Found {} step configurations", stepconfigs.size());
        logger.info("Step configurations: {}", stepconfigs);
        model.addAttribute("steps", stepconfigs);
        return "stepconfig/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        logger.info("Showing add step configuration form");
        model.addAttribute("stepConfig", new StepConfig());
        return "stepconfig/add";
    }

    @PostMapping("/save")
    public String saveStepConfig(@ModelAttribute StepConfig stepConfig) {
        logger.info("Saving step configuration: {}", stepConfig.getStepName());
        stepConfigRepository.save(stepConfig);
        return "redirect:/stepconfig/list";
    }

    @GetMapping("/edit/{stepName}")
    public String showEditForm(@PathVariable String stepName, Model model) {
        logger.info("Showing edit form for step: {}", stepName);
        StepConfig stepConfig = stepConfigRepository.findById(stepName)
            .orElseThrow(() -> new IllegalArgumentException("Invalid step name: " + stepName));
        model.addAttribute("stepConfig", stepConfig);
        return "stepconfig/edit";
    }

    @PostMapping("/update")
    public String updateStepConfig(@ModelAttribute StepConfig stepConfig) {
        logger.info("Updating step configuration: {}", stepConfig.getStepName());
        stepConfigRepository.save(stepConfig);
        return "redirect:/stepconfig/list";
    }

    @GetMapping("/delete/{stepName}")
    public String deleteStepConfig(@PathVariable String stepName) {
        logger.info("Deleting step configuration: {}", stepName);
        stepConfigRepository.deleteById(stepName);
        return "redirect:/stepconfig/list";
    }
}
