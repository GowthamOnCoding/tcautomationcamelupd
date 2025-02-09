package com.boa.tcautomation.route;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Map;

//@Component
public class TestCaseProcessorRoute extends RouteBuilder {

    private final DataSource dataSource;

    public TestCaseProcessorRoute(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void configure() {
        errorHandler(deadLetterChannel("direct:error")
                .maximumRedeliveries(3)
                .redeliveryDelay(1000)
                .backOffMultiplier(2)
                .useExponentialBackOff());

        from("timer://runOnce?repeatCount=1&delay=1000") // Trigger the route once
                .setBody(constant("SELECT * FROM TC_MASTER")) // SQL query
                .to("jdbc:dataSource") // JDBC component with the DataSource
                .split(body())
                .streaming()
                .process(exchange -> {
                    Map<String, Object> body = exchange.getIn().getBody(Map.class);
                    if (body != null) {
                        log.info("Body: " + body);
                        if (body.get("TC_ID") != null) {
                            String tcId = body.get("TC_ID").toString();
                            log.info("TC ID: " + tcId);
                            Thread.startVirtualThread(() -> {
                                processTestCase(tcId);
                            });
                        } else {
                            log.error("TC_ID is null");
                        }
                    } else {
                        log.error("The body is null");
                    }
                });

        from("direct:error")
                .process(exchange -> {
                    Exception cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
                    log.error("Processing failed", cause);
                })
                .to("log:error");
    }

    private void processTestCase(String tcId) {
        // Implementation
        log.info("Processing test case: " + tcId);
    }
}
