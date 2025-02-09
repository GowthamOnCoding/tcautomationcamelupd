package com.boa.tcautomation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.apache.camel.spring.boot.CamelAutoConfiguration;

@SpringBootApplication(scanBasePackages = "com.boa.tcautomation")
@org.springframework.context.annotation.Import(CamelAutoConfiguration.class)
public class TcautomationApplication {

    public static void main(String[] args) {
        SpringApplication.run(TcautomationApplication.class, args);
    }
}
