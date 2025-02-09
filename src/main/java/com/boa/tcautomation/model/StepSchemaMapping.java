// StepSchemaMapping.java
package com.boa.tcautomation.model;

import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "STEP_SCHEMA_MAPPING")
@IdClass(StepSchemaMappingId.class)
public class StepSchemaMapping {
    @Id
    private String stepName;
    @Id
    private String schemaId;
    private boolean isRequired;
    private Integer sequenceNo;
    private LocalDateTime createdDate;
}
