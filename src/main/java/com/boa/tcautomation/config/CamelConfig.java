package com.boa.tcautomation.config;

import jakarta.annotation.PostConstruct;
import org.apache.camel.CamelContext;
import org.apache.camel.spring.boot.CamelContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.camel.component.jdbc.JdbcComponent;

import javax.sql.DataSource;

@Configuration
public class CamelConfig {
    @Autowired
    private CamelContext camelContext;

    @Autowired
    private DataSource dataSource;

    @Bean
    public CamelContextConfiguration contextConfiguration() {
        return new CamelContextConfiguration() {
            @Override
            public void beforeApplicationStart(CamelContext camelContext) {
                // You can add global configurations here if needed
            }

            @Override
            public void afterApplicationStart(CamelContext camelContext) {
                System.out.println("Total routes: " + camelContext.getRoutes().size());
                camelContext.getRoutes().forEach(r -> System.out.println("Route: " + r));
            }
        };
    }
   @Bean
    public JdbcComponent jdbcComponent(DataSource dataSource) {
        JdbcComponent jdbcComponent = new JdbcComponent();
        jdbcComponent.setDataSource(dataSource);
        return jdbcComponent;
    }
}