package com.boa.tcautomation.util;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.springframework.stereotype.Component;

@Component
public class FileMoverUtil {

    private final CamelContext camelContext;
    private final ProducerTemplate producerTemplate;

    public FileMoverUtil() throws Exception {
        this.camelContext = new DefaultCamelContext();
        this.producerTemplate = camelContext.createProducerTemplate();
        camelContext.start();
    }

    public void moveFile(String source, String destination) throws Exception {
        camelContext.addRoutes(new RouteBuilder() {
            @Override
            public void configure() {
                from("file:" + source + "?noop=true")
                        .to("file:" + destination);
            }
        });

        producerTemplate.sendBody("file:" + source, null);
    }
}
