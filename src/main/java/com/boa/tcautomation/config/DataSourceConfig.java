package com.boa.tcautomation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .url("jdbc:mysql://localhost:3306/testdb")
                .username("root")
                .password("root")
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .build();
    }
}
