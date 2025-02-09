package com.boa.tcautomation.route;

import com.boa.tcautomation.model.TcSteps;
import com.boa.tcautomation.service.DatabaseService;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

//@Component
public class TcStepsRoute extends RouteBuilder {

    @Autowired
    private DatabaseService databaseService;

    @Override
    public void configure() {
        from("direct:addTcSteps")
                .process(exchange -> {
                    Map<String, Object> rowData = exchange.getIn().getBody(Map.class);
                    databaseService.addRow("tc_steps", rowData);
                });

        from("direct:deleteTcSteps")
                .process(exchange -> {
                    Integer stepId = exchange.getIn().getHeader("stepId", Integer.class);
                    String tcId = exchange.getIn().getHeader("tcId", String.class);
                    databaseService.deleteRow("tc_steps", "step_id", stepId);
                    databaseService.deleteRow("tc_steps", "tc_id", tcId);
                });

        from("direct:updateTcSteps")
                .process(exchange -> {
                    Integer stepId = exchange.getIn().getHeader("stepId", Integer.class);
                    String tcId = exchange.getIn().getHeader("tcId", String.class);
                    Map<String, Object> updatedData = exchange.getIn().getBody(Map.class);
                    databaseService.updateRow("tc_steps", "step_id", stepId, updatedData);
                    databaseService.updateRow("tc_steps", "tc_id", tcId, updatedData);
                });

        from("direct:cloneTcSteps")
                .process(exchange -> {
                    Integer stepId = exchange.getIn().getHeader("stepId", Integer.class);
                    String tcId = exchange.getIn().getHeader("tcId", String.class);
                    databaseService.cloneRow("tc_steps", "step_id", stepId);
                    databaseService.cloneRow("tc_steps", "tc_id", tcId);
                });

        from("direct:insertTcSteps")
                .process(exchange -> {
                    Map<String, Object> rowData = exchange.getIn().getBody(Map.class);
                    databaseService.insertRow("tc_steps", rowData);
                });

        from("direct:selectTcSteps")
                .process(exchange -> {
                    Integer stepId = exchange.getIn().getHeader("stepId", Integer.class);
                    TcSteps result = databaseService.selectRow("tc_steps", "step_id", stepId, TcSteps.class);
                    exchange.getIn().setBody(result);
                });
    }
}
