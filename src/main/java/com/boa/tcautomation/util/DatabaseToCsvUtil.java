package com.boa.tcautomation.util;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.CamelContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Component
public class DatabaseToCsvUtil extends RouteBuilder {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private CamelContext camelContext;

    @Override
    public void configure() throws Exception {
        // Error handling
        onException(Exception.class)
                .log("Error occurred: ${exception.message}")
                .handled(true)
                .end();

        from("direct:queryToCsv")
                .routeId("queryToCsvRoute")
                .log("Executing query: ${body}")
                .to("jdbc:dataSource")
                .marshal().csv()
                .log("Writing to file: ${header.destination}/${header.fileName}")
                .toD("file:${header.destination}?fileName=${header.fileName}"); // Use headers for destination and fileName
    }

    public void queryToCsv(String query, String destination, String fileName) throws Exception {
        // Create a message with the query, destination, and fileName
        Map<String, Object> headers = new HashMap<>();
        headers.put("destination", destination);
        headers.put("fileName", fileName);
        log.info("Querying database and writing to CSV file: {}", headers);

        // Trigger the route
        camelContext.createProducerTemplate().sendBodyAndHeaders("direct:queryToCsv", query, headers);
    }
}
