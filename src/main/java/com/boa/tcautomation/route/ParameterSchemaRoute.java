package com.boa.tcautomation.route;

import com.boa.tcautomation.model.ParameterSchema;
import com.boa.tcautomation.service.DatabaseService;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

//@Component
public class ParameterSchemaRoute extends RouteBuilder {

    @Autowired
    private DatabaseService databaseService;

    @Override
    public void configure() {
        from("direct:addParameterSchema")
                .process(exchange -> {
                    Map<String, Object> rowData = exchange.getIn().getBody(Map.class);
                    databaseService.addRow("parameter_schema", rowData);
                });

        from("direct:deleteParameterSchema")
                .process(exchange -> {
                    String schemaId = exchange.getIn().getHeader("schemaId", String.class);
                    databaseService.deleteRow("parameter_schema", "schema_id", schemaId);
                });

        from("direct:updateParameterSchema")
                .process(exchange -> {
                    String schemaId = exchange.getIn().getHeader("schemaId", String.class);
                    Map<String, Object> updatedData = exchange.getIn().getBody(Map.class);
                    databaseService.updateRow("parameter_schema", "schema_id", schemaId, updatedData);
                });

        from("direct:cloneParameterSchema")
                .process(exchange -> {
                    String schemaId = exchange.getIn().getHeader("schemaId", String.class);
                    databaseService.cloneRow("parameter_schema", "schema_id", schemaId);
                });

        from("direct:insertParameterSchema")
                .process(exchange -> {
                    Map<String, Object> rowData = exchange.getIn().getBody(Map.class);
                    databaseService.insertRow("parameter_schema", rowData);
                });

        from("direct:selectParameterSchema")
                .process(exchange -> {
                    String schemaId = exchange.getIn().getHeader("schemaId", String.class);
                    ParameterSchema result = databaseService.selectRow("parameter_schema", "schema_id", schemaId, ParameterSchema.class);
                    exchange.getIn().setBody(result);
                });
    }
}
