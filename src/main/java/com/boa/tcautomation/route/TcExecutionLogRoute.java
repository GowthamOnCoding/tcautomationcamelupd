package com.boa.tcautomation.route;

import com.boa.tcautomation.model.TcExecutionLog;
import com.boa.tcautomation.service.DatabaseService;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

//@Component
public class TcExecutionLogRoute extends RouteBuilder {

    @Autowired
    private DatabaseService databaseService;

    @Override
    public void configure() {
        from("direct:addTcExecutionLog")
                .process(exchange -> {
                    Map<String, Object> rowData = exchange.getIn().getBody(Map.class);
                    databaseService.addRow("tc_execution_log", rowData);
                });

        from("direct:deleteTcExecutionLog")
                .process(exchange -> {
                    Long executionId = exchange.getIn().getHeader("executionId", Long.class);
                    databaseService.deleteRow("tc_execution_log", "execution_id", executionId);
                });

        from("direct:updateTcExecutionLog")
                .process(exchange -> {
                    Long executionId = exchange.getIn().getHeader("executionId", Long.class);
                    Map<String, Object> updatedData = exchange.getIn().getBody(Map.class);
                    databaseService.updateRow("tc_execution_log", "execution_id", executionId, updatedData);
                });

        from("direct:cloneTcExecutionLog")
                .process(exchange -> {
                    Long executionId = exchange.getIn().getHeader("executionId", Long.class);
                    databaseService.cloneRow("tc_execution_log", "execution_id", executionId);
                });

        from("direct:insertTcExecutionLog")
                .process(exchange -> {
                    Map<String, Object> rowData = exchange.getIn().getBody(Map.class);
                    databaseService.insertRow("tc_execution_log", rowData);
                });

        from("direct:selectTcExecutionLog")
                .process(exchange -> {
                    Long executionId = exchange.getIn().getHeader("executionId", Long.class);
                    TcExecutionLog result = databaseService.selectRow("tc_execution_log", "execution_id", executionId, TcExecutionLog.class);
                    exchange.getIn().setBody(result);
                });
    }
}
