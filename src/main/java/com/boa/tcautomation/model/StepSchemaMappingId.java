// StepSchemaMappingId.java
package com.boa.tcautomation.model;

import java.io.Serializable;
import java.util.Objects;

public class StepSchemaMappingId implements Serializable {
    private String stepName;
    private String schemaId;

    // Default constructor
    public StepSchemaMappingId() {}

    // Parameterized constructor
    public StepSchemaMappingId(String stepName, String schemaId) {
        this.stepName = stepName;
        this.schemaId = schemaId;
    }

    // Getters, setters, equals, and hashCode methods
    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public String getSchemaId() {
        return schemaId;
    }

    public void setSchemaId(String schemaId) {
        this.schemaId = schemaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSchemaMappingId that = (StepSchemaMappingId) o;
        return Objects.equals(stepName, that.stepName) && Objects.equals(schemaId, that.schemaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stepName, schemaId);
    }
}
