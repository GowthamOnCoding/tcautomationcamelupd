package com.boa.tcautomation.validation;

import com.boa.tcautomation.validation.QueryValidationParams;
import com.boa.tcautomation.validation.ValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResultValidationService {
    private static final Logger log = LoggerFactory.getLogger(ResultValidationService.class);

    public ValidationResult validateResult(Object actualResult, QueryValidationParams params) {
        log.debug("Validating result: actual={}, expected={}, comparison={}",
                actualResult, params.getExpectedOutput(), params.getComparisonType());

        if (actualResult == null && params.getExpectedOutput() == null) {
            return ValidationResult.success("Both actual and expected results are null");
        }

        if (actualResult == null) {
            return ValidationResult.failure("Actual result is null", null, params.getExpectedOutput());
        }

        if (params.getExpectedOutput() == null) {
            return ValidationResult.failure("Expected output is null", actualResult, null);
        }

        // Handle list results
        if (actualResult instanceof List) {
            return validateListResult((List<?>) actualResult, params);
        }

        // Handle single values
        return validateSingleResult(actualResult, params.getExpectedOutput(), params);
    }

    private ValidationResult validateListResult(List<?> resultList, QueryValidationParams params) {
        String comparisonType = params.getComparisonType();

        if ("count".equalsIgnoreCase(comparisonType)) {
            int actualCount = resultList.size();
            int expectedCount = Integer.parseInt(params.getExpectedOutput().toString());
            if (actualCount == expectedCount) {
                return ValidationResult.success("Row count matches expected value");
            } else {
                return ValidationResult.failure("Row count mismatch", actualCount, expectedCount);
            }
        } else if ("contains".equalsIgnoreCase(comparisonType)) {
            Object expectedItem = params.getExpectedOutput();
            boolean found = false;

            for (Object item : resultList) {
                if (compareValues(item, expectedItem, params)) {
                    found = true;
                    break;
                }
            }

            if (found) {
                return ValidationResult.success("List contains expected item");
            } else {
                return ValidationResult.failure("List does not contain expected item", resultList, expectedItem);
            }
        } else if ("notEmpty".equalsIgnoreCase(comparisonType)) {
            if (!resultList.isEmpty()) {
                return ValidationResult.success("List is not empty");
            } else {
                return ValidationResult.failure("List is empty", resultList, "Non-empty list");
            }
        } else if ("isEmpty".equalsIgnoreCase(comparisonType)) {
            if (resultList.isEmpty()) {
                return ValidationResult.success("List is empty");
            } else {
                return ValidationResult.failure("List is not empty", resultList, "Empty list");
            }
        }

        // Default handling for unrecognized comparison type with lists
        return ValidationResult.failure("Unsupported comparison type for lists: " + comparisonType, resultList, params.getExpectedOutput());
    }

    private ValidationResult validateSingleResult(Object actual, Object expected, QueryValidationParams params) {
        return compareValues(actual, expected, params)
                ? ValidationResult.success("Value matches expected result")
                : ValidationResult.failure("Value does not match expected result", actual, expected);
    }

    public boolean compareValues(Object actual, Object expected, QueryValidationParams params) {
        if (actual == null && expected == null) {
            return true;
        }

        if (actual == null || expected == null) {
            return false;
        }

        String comparisonType = params.getComparisonType();

        // String comparisons with optional case insensitivity
        if (actual instanceof String && expected instanceof String) {
            return compareStringValues((String)actual, (String)expected, comparisonType, params.isIgnoreCase());
        }

        // Numeric comparisons with tolerance
        if (actual instanceof Number && expected instanceof Number) {
            return compareNumericValues((Number)actual, (Number)expected, comparisonType, params.getTolerance());
        }

        // Boolean comparison
        if (actual instanceof Boolean && expected instanceof Boolean) {
            return actual.equals(expected);
        }

        // Default comparison
        if ("equals".equalsIgnoreCase(comparisonType)) {
            return actual.equals(expected);
        }

        // If unknown comparison type or incompatible types
        log.warn("Unsupported comparison type or incompatible types: actual={}, expected={}, comparison={}",
                actual.getClass().getName(), expected.getClass().getName(), comparisonType);
        return false;
    }

    private boolean compareStringValues(String actual, String expected, String comparisonType, boolean ignoreCase) {
        if (ignoreCase) {
            switch(comparisonType.toLowerCase()) {
                case "equals": return actual.equalsIgnoreCase(expected);
                case "contains": return actual.toLowerCase().contains(expected.toLowerCase());
                case "startswith": return actual.toLowerCase().startsWith(expected.toLowerCase());
                case "endswith": return actual.toLowerCase().endsWith(expected.toLowerCase());
                default: return false;
            }
        } else {
            switch(comparisonType.toLowerCase()) {
                case "equals": return actual.equals(expected);
                case "contains": return actual.contains(expected);
                case "startswith": return actual.startsWith(expected);
                case "endswith": return actual.endsWith(expected);
                default: return false;
            }
        }
    }

    private boolean compareNumericValues(Number actual, Number expected, String comparisonType, double tolerance) {
        double actualNum = actual.doubleValue();
        double expectedNum = expected.doubleValue();

        switch(comparisonType.toLowerCase()) {
            case "equals": return Math.abs(actualNum - expectedNum) <= tolerance;
            case "greaterthan": return actualNum > expectedNum;
            case "lessthan": return actualNum < expectedNum;
            case "greaterorequal": return actualNum >= expectedNum;
            case "lessorequal": return actualNum <= expectedNum;
            default: return false;
        }
    }
}
