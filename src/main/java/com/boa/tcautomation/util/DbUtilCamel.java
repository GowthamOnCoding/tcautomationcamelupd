package com.boa.tcautomation.util;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultCamelContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class DbUtilCamel {

    private final CamelContext camelContext;
    private final ProducerTemplate producerTemplate;

    public DbUtilCamel() throws Exception {
        this.camelContext = new DefaultCamelContext();
        this.producerTemplate = camelContext.createProducerTemplate();
        camelContext.start();
    }

    public void addRow(String tableName, Map<String, Object> rowData) {
        String columns = String.join(", ", rowData.keySet());
        String values = String.join(", ", rowData.values().stream().map(value -> "'" + value + "'").toArray(String[]::new));
        String sql = "INSERT INTO " + tableName + " (" + columns + ") VALUES (" + values + ")";
        producerTemplate.sendBody("direct:addRow", sql);
    }

    public void deleteRow(String tableName, String columnName, Object value) {
        String sql = "DELETE FROM " + tableName + " WHERE " + columnName + " = '" + value + "'";
        producerTemplate.sendBody("direct:deleteRow", sql);
    }

    public void updateRow(String tableName, String columnName, Object value, Map<String, Object> updatedData) {
        String setClause = String.join(", ", updatedData.entrySet().stream()
                .map(entry -> entry.getKey() + " = '" + entry.getValue() + "'")
                .toArray(String[]::new));
        String sql = "UPDATE " + tableName + " SET " + setClause + " WHERE " + columnName + " = '" + value + "'";
        producerTemplate.sendBody("direct:updateRow", sql);
    }

    public void cloneRow(String tableName, String columnName, Object value) {
        String sql = "INSERT INTO " + tableName + " SELECT * FROM " + tableName + " WHERE " + columnName + " = '" + value + "'";
        producerTemplate.sendBody("direct:cloneRow", sql);
    }

    public void insertRow(String tableName, Map<String, Object> rowData) {
        addRow(tableName, rowData);
    }

    public <T> T selectRow(String tableName, String columnName, Object value, Class<T> mappingClass) {
        String sql = "SELECT * FROM " + tableName + " WHERE " + columnName + " = ?";
        return producerTemplate.requestBody("direct:selectRow", sql, mappingClass);
    }

    public void executeQuery(String sql) {
        producerTemplate.sendBody("direct:executeQuery", sql);
    }

    public List<Map<String, Object>> queryForList(String sql) {
        return producerTemplate.requestBody("direct:queryForList", sql, List.class);
    }

    public <T> T queryForObject(String sql, Class<T> requiredType) {
        return producerTemplate.requestBody("direct:queryForObject", sql, requiredType);
    }
}
