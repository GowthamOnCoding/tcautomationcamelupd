package com.boa.tcautomation.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Component
public class DbUtil {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final Logger log = Logger.getLogger(DbUtil.class.getName());

    public boolean addRow(String tableName, Map<String, Object> rowData) {
        try {
            String columns = String.join(", ", rowData.keySet());
            String values = String.join(", ", rowData.values().stream().map(value -> "'" + value + "'").toArray(String[]::new));
            String sql = "INSERT INTO " + tableName + " (" + columns + ") VALUES (" + values + ")";
            log.info("Executing addRow SQL: " + sql);
            return executeQuery(sql);
        } catch (Exception e) {
            log.severe("Error adding row: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteRow(String tableName, String columnName, Object value) {
        try {
            String sql = "DELETE FROM " + tableName + " WHERE " + columnName + " = '" + value + "'";
            log.info("Executing deleteRow SQL: " + sql);
            return executeQuery(sql);
        } catch (Exception e) {
            log.severe("Error deleting row: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateRow(String tableName, String columnName, Object value, Map<String, Object> updatedData) {
        try {
            String setClause = String.join(", ", updatedData.entrySet().stream()
                    .map(entry -> entry.getKey() + " = '" + entry.getValue() + "'")
                    .toArray(String[]::new));
            String sql = "UPDATE " + tableName + " SET " + setClause + " WHERE " + columnName + " = '" + value + "'";
            log.info("Executing updateRow SQL: " + sql);
            return executeQuery(sql);
        } catch (Exception e) {
            log.severe("Error updating row: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean cloneRow(String tableName, String columnName, Object value) {
        try {
            String sql = "INSERT INTO " + tableName + " SELECT * FROM " + tableName + " WHERE " + columnName + " = '" + value + "'";
            log.info("Executing cloneRow SQL: " + sql);
            return executeQuery(sql);
        } catch (Exception e) {
            log.severe("Error cloning row: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public <T> boolean insertRow(String tableName, T rowData) {
        try {
            Map<String, Object> rowMap = convertToMap(rowData);
            log.info("Inserting row into table: " + tableName + " with data: " + rowMap);
            return addRow(tableName, rowMap);
        } catch (Exception e) {
            log.severe("Error inserting row: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private <T> Map<String, Object> convertToMap(T rowData) {
        Map<String, Object> rowMap = new HashMap<>();
        for (Field field : rowData.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                rowMap.put(field.getName(), field.get(rowData));
            } catch (IllegalAccessException e) {
                log.severe("Error accessing field value: " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("Error accessing field value", e);
            }
        }
        log.info("Converted object to map: " + rowMap);
        return rowMap;
    }

    public <T> T selectRow(String tableName, String columnName, Object value, Class<T> mappingClass) {
        try {
            String sql = "SELECT * FROM " + tableName + " WHERE " + columnName + " = ?";
            log.info("Executing selectRow SQL: " + sql);
            return jdbcTemplate.queryForObject(sql, new Object[]{value}, new BeanPropertyRowMapper<>(mappingClass));
        } catch (Exception e) {
            log.severe("Error selecting row: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public <T> List<T> queryForListWithMapping(String sql, Class<T> mappingClass) {
        try {
            log.info("Executing queryForListWithMapping SQL: " + sql);
            return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(mappingClass));
        } catch (Exception e) {
            log.severe("Error querying for list with mapping: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public boolean executeQuery(String sql) {
        try {
            log.info("Executing query: " + sql);
            jdbcTemplate.execute(sql);
            return true;
        } catch (Exception e) {
            log.severe("Error executing query: " + sql + " - " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<Map<String, Object>> queryForList(String sql) {
        try {
            log.info("Executing queryForList SQL: " + sql);
            return jdbcTemplate.queryForList(sql);
        } catch (Exception e) {
            log.severe("Error querying for list: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

   public <T> T queryForObject(String sql, Class<T> requiredType) {
    try {
        log.info("Executing queryForObject SQL: " + sql);
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(requiredType));
    } catch (Exception e) {
        log.severe("Error executing queryForObject: " + e.getMessage());
        e.printStackTrace();
        return null;
    }
}
    public int update(String sql, Object... args) {
        return jdbcTemplate.update(sql, args);
    }
}
