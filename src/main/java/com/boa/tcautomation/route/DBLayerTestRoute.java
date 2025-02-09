package com.boa.tcautomation.route;

import com.boa.tcautomation.service.DatabaseService;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

//@Component
public class DBLayerTestRoute extends RouteBuilder {

    @Autowired
    private DatabaseService databaseService;

    @Override
    public void configure() {
        errorHandler(deadLetterChannel("direct:error")
                .maximumRedeliveries(3)
                .redeliveryDelay(1000)
                .backOffMultiplier(2)
                .useExponentialBackOff());

        from("timer://runOnce?repeatCount=1&delay=1000")
                .process(exchange -> {
                    // Example usage of DatabaseService methods
                    Map<String, Object> rowData = new HashMap<>();
                    rowData.put("column1", "value1");
                    rowData.put("column2", "value2");

                    // Add a row
                    databaseService.addRow("tableName", rowData);

                    // Delete a row
                    databaseService.deleteRow("tableName", "column1", "value1");

                    // Update a row
                    Map<String, Object> updatedData = new HashMap<>();
                    updatedData.put("column2", "newValue2");
                    databaseService.updateRow("tableName", "column1", "value1", updatedData);

                    // Clone a row
                    databaseService.cloneRow("tableName", "column1", "value1");

                    // Insert a row
                    databaseService.insertRow("tableName", rowData);

                    // Select a row
                    MyClass result = databaseService.selectRow("tableName", "column1", "value1", MyClass.class);
                    exchange.getIn().setBody(result);
                });

        from("direct:error")
                .process(exchange -> {
                    Exception cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
                    log.error("Processing failed", cause);
                })
                .to("log:error");
    }

    // Define MyClass as a mapping class for the selectRow method
    public static class MyClass {
        private String column1;
        private String column2;

        // Getters and setters
        public String getColumn1() {
            return column1;
        }

        public void setColumn1(String column1) {
            this.column1 = column1;
        }

        public String getColumn2() {
            return column2;
        }

        public void setColumn2(String column2) {
            this.column2 = column2;
        }
    }
}
