package com.boa.tcautomation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DatabaseService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void addRow(String tableName, Map<String, Object> rowData) {
        String columns = String.join(", ", rowData.keySet());
        String values = String.join(", ", rowData.values().stream().map(value -> "'" + value + "'").toArray(String[]::new));
        String sql = "INSERT INTO " + tableName + " (" + columns + ") VALUES (" + values + ")";
        jdbcTemplate.execute(sql);
    }

    public void deleteRow(String tableName, String columnName, Object value) {
        String sql = "DELETE FROM " + tableName + " WHERE " + columnName + " = '" + value + "'";
        jdbcTemplate.execute(sql);
    }

    public void updateRow(String tableName, String columnName, Object value, Map<String, Object> updatedData) {
        String setClause = String.join(", ", updatedData.entrySet().stream()
                .map(entry -> entry.getKey() + " = '" + entry.getValue() + "'")
                .toArray(String[]::new));
        String sql = "UPDATE " + tableName + " SET " + setClause + " WHERE " + columnName + " = '" + value + "'";
        jdbcTemplate.execute(sql);
    }

    public void cloneRow(String tableName, String columnName, Object value) {
        String sql = "INSERT INTO " + tableName + " SELECT * FROM " + tableName + " WHERE " + columnName + " = '" + value + "'";
        jdbcTemplate.execute(sql);
    }

    public void insertRow(String tableName, Map<String, Object> rowData) {
        addRow(tableName, rowData);
    }

    public <T> T selectRow(String tableName, String columnName, Object value, Class<T> mappingClass) {
        String sql = "SELECT * FROM " + tableName + " WHERE " + columnName + " = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{value}, new BeanPropertyRowMapper<>(mappingClass));
    }
}
