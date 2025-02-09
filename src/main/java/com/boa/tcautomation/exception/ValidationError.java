// ValidationError.java
package com.boa.tcautomation.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

//@Data
//@AllArgsConstructor
public class ValidationError {
    private String schemaId;
    private Object errorDetails;

    // No-args constructor
    public ValidationError() {
    }

    // All-args constructor
    public ValidationError(String schemaId, Object errorDetails) {
        this.schemaId = schemaId;
        this.errorDetails = errorDetails;
    }

    // Getters and Setters
    public String getSchemaId() {
        return schemaId;
    }

    public void setSchemaId(String schemaId) {
        this.schemaId = schemaId;
    }

    public Object getErrorDetails() {
        return errorDetails;
    }

    public void setErrorDetails(Object errorDetails) {
        this.errorDetails = errorDetails;
    }
}
