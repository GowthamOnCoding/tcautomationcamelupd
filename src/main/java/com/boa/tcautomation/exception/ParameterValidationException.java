// ParameterValidationException.java
package com.boa.tcautomation.exception;

import java.util.List;

public class ParameterValidationException extends RuntimeException {
    private final String stepName;
    private final List<ValidationError> errors;

    public ParameterValidationException(String stepName, List<ValidationError> errors) {
        super("Validation failed for step: " + stepName);
        this.stepName = stepName;
        this.errors = errors;
    }

    public String getStepName() {
        return stepName;
    }

    public List<ValidationError> getErrors() {
        return errors;
    }
}
