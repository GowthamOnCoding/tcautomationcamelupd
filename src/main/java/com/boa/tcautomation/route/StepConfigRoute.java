package com.boa.tcautomation.route;

import com.boa.tcautomation.model.StepConfig;
import com.boa.tcautomation.service.DatabaseService;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

//@Component
public class StepConfigRoute extends RouteBuilder {

    @Autowired
    private DatabaseService databaseService;

    @Override
    public void configure() {
        from("direct:addStepConfig")
                .process(exchange -> {
                    Map<String, Object> rowData = exchange.getIn().getBody(Map.class);
                    databaseService.addRow("step_config", rowData);
                });

        from("direct:deleteStepConfig")
                .process(exchange -> {
                    String stepName = exchange.getIn().getHeader("stepName", String.class);
                    databaseService.deleteRow("step_config", "step_name", stepName);
                });

        from("direct:updateStepConfig")
                .process(exchange -> {
                    String stepName = exchange.getIn().getHeader("stepName", String.class);
                    Map<String, Object> updatedData = exchange.getIn().getBody(Map.class);
                    databaseService.updateRow("step_config", "step_name", stepName, updatedData);
                });

        from("direct:cloneStepConfig")
                .process(exchange -> {
                    String stepName = exchange.getIn().getHeader("stepName", String.class);
                    databaseService.cloneRow("step_config", "step_name", stepName);
                });

        from("direct:insertStepConfig")
                .process(exchange -> {
                    Map<String, Object> rowData = exchange.getIn().getBody(Map.class);
                    databaseService.insertRow("step_config", rowData);
                });

        from("direct:selectStepConfig")
                .process(exchange -> {
                    String stepName = exchange.getIn().getHeader("stepName", String.class);
                    StepConfig result = databaseService.selectRow("step_config", "step_name", stepName, StepConfig.class);
                    exchange.getIn().setBody(result);
                });
    }
}
