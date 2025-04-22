package com.boa.tcautomation.validation;

import com.boa.tcautomation.model.TcMaster;
import com.boa.tcautomation.validation.QueryValidationParams;
import com.boa.tcautomation.validation.ValidationResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class QueryValidationFacade {
    private static final Logger log = LoggerFactory.getLogger(QueryValidationFacade.class);

    @Autowired
    private QueryPlaceholderService placeholderService;

    @Autowired
    private QueryExecutionService executionService;

    @Autowired
    private ResultValidationService validationService;

    public ValidationResult validateQuery(String parametersJson, TcMaster tcMaster) throws IOException {
        // Parse parameters
        QueryValidationParams params = parseValidationParams(parametersJson);

        // Process placeholders
        String processedQuery = placeholderService.processQueryPlaceholders(params.getQuery(), tcMaster);

        // Execute query
        Object result = executionService.executeQuery(processedQuery, params);

        // Validate result
        return validationService.validateResult(result, params);
    }

    private QueryValidationParams parseValidationParams(String parametersJson) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.readValue(parametersJson, QueryValidationParams.class);
    }
}
