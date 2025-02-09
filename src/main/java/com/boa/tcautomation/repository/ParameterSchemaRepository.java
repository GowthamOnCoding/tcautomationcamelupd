// ParameterSchemaRepository.java
package com.boa.tcautomation.repository;

import com.boa.tcautomation.model.ParameterSchema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParameterSchemaRepository extends JpaRepository<ParameterSchema, String> {
    @Query("""
        SELECT ps FROM ParameterSchema ps
        JOIN StepSchemaMapping ssm ON ps.schemaId = ssm.schemaId
        WHERE ssm.stepName = :stepName AND ps.isActive = true
        ORDER BY ssm.sequenceNo
    """)
    List<ParameterSchema> findActiveSchemasByStepName(String stepName);
}
