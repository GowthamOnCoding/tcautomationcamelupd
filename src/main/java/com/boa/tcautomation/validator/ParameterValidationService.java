// ParameterValidationService.java
package com.boa.tcautomation.validator;

import com.boa.tcautomation.exception.ParameterValidationException;
import com.boa.tcautomation.exception.SchemaConfigurationException;
import com.boa.tcautomation.exception.ValidationError;
import com.boa.tcautomation.model.ParameterSchema;
import com.boa.tcautomation.repository.ParameterSchemaRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
@Service
@Slf4j
public class ParameterValidationService {
    private final JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
    private final Map<String, JsonSchema> schemaCache = new ConcurrentHashMap<>();
    private final ParameterSchemaRepository schemaRepository;

    public ParameterValidationService(ParameterSchemaRepository schemaRepository) {
        this.schemaRepository = schemaRepository;
    }

    public void validateParameters(String stepName, Map<String, String> parameters) {
        List<ParameterSchema> schemas = schemaRepository.findActiveSchemasByStepName(stepName);
        List<ValidationError> errors = new ArrayList<>();

        for (ParameterSchema schema : schemas) {
            try {
                JsonSchema jsonSchema = getOrCreateSchema(schema);
                JsonNode parametersNode = new ObjectMapper().valueToTree(parameters);
                ProcessingReport report = jsonSchema.validate(parametersNode);

                if (!report.isSuccess()) {
                    errors.add(new ValidationError(schema.getSchemaId(), report));
                }
            } catch (Exception e) {
                errors.add(new ValidationError(schema.getSchemaId(), e.getMessage()));
            }
        }

        if (!errors.isEmpty()) {
            throw new ParameterValidationException(stepName, errors);
        }
    }

    private JsonSchema getOrCreateSchema(ParameterSchema schema) {
        String cacheKey = schema.getSchemaId() + "_v" + schema.getSchemaVersion();
        return schemaCache.computeIfAbsent(cacheKey, k -> {
            try {
                return factory.getJsonSchema(JsonLoader.fromString(schema.getSchemaDefinition()));
            } catch (Exception e) {
                throw new SchemaConfigurationException("Invalid schema: " + schema.getSchemaId(), e);
            }
        });
    }
}
