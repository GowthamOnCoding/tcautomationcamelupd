package com.boa.tcautomation.validation;

public class ValidationResult {
    private boolean valid;
    private String message;
    private Object actualResult;
    private Object expectedResult;

    public ValidationResult(boolean valid, String message, Object actualResult, Object expectedResult) {
        this.valid = valid;
        this.message = message;
        this.actualResult = actualResult;
        this.expectedResult = expectedResult;
    }

    public static ValidationResult success(String message) {
        return new ValidationResult(true, message, null, null);
    }

    public static ValidationResult failure(String message, Object actual, Object expected) {
        return new ValidationResult(false, message, actual, expected);
    }

    public boolean isValid() { return valid; }
    public String getMessage() { return message; }
    public Object getActualResult() { return actualResult; }
    public Object getExpectedResult() { return expectedResult; }
}
