package com.boa.tcautomation.route;

import com.boa.tcautomation.model.StepSchemaMapping;
import com.boa.tcautomation.service.DatabaseService;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

//@Component
public class StepSchemaMappingRoute extends RouteBuilder {

    @Autowired
    private DatabaseService databaseService;

    @Override
    public void configure() {
        from("direct:addStepSchemaMapping")
                .process(exchange -> {
                    Map<String, Object> rowData = exchange.getIn().getBody(Map.class);
                    databaseService.addRow("step_schema_mapping", rowData);
                });

        from("direct:deleteStepSchemaMapping")
                .process(exchange -> {
                    String stepName = exchange.getIn().getHeader("stepName", String.class);
                    String schemaId = exchange.getIn().getHeader("schemaId", String.class);
                    databaseService.deleteRow("step_schema_mapping", "step_name", stepName);
                    databaseService.deleteRow("step_schema_mapping", "schema_id", schemaId);
                });

        from("direct:updateStepSchemaMapping")
                .process(exchange -> {
                    String stepName = exchange.getIn().getHeader("stepName", String.class);
                    String schemaId = exchange.getIn().getHeader("schemaId", String.class);
                    Map<String, Object> updatedData = exchange.getIn().getBody(Map.class);
                    databaseService.updateRow("step_schema_mapping", "step_name", stepName, updatedData);
                    databaseService.updateRow("step_schema_mapping", "schema_id", schemaId, updatedData);
                });

        from("direct:cloneStepSchemaMapping")
                .process(exchange -> {
                    String stepName = exchange.getIn().getHeader("stepName", String.class);
                    String schemaId = exchange.getIn().getHeader("schemaId", String.class);
                    databaseService.cloneRow("step_schema_mapping", "step_name", stepName);
                    databaseService.cloneRow("step_schema_mapping", "schema_id", schemaId);
                });

        from("direct:insertStepSchemaMapping")
                .process(exchange -> {
                    Map<String, Object> rowData = exchange.getIn().getBody(Map.class);
                    databaseService.insertRow("step_schema_mapping", rowData);
                });

        from("direct:selectStepSchemaMapping")
                .process(exchange -> {
                    String stepName = exchange.getIn().getHeader("stepName", String.class);
                    String schemaId = exchange.getIn().getHeader("schemaId", String.class);
                    StepSchemaMapping result = databaseService.selectRow("step_schema_mapping", "step_name", stepName, StepSchemaMapping.class);
                    exchange.getIn().setBody(result);
                });
    }
}
