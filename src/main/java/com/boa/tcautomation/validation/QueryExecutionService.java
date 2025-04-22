package com.boa.tcautomation.validation;

import com.boa.tcautomation.validation.QueryValidationParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QueryExecutionService {
    private static final Logger log = LoggerFactory.getLogger(QueryExecutionService.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public Object executeQuery(String query, QueryValidationParams params) {
        log.debug("Executing query: {}", query);

        if ("list".equalsIgnoreCase(params.getResultType())) {
            return executeListQuery(query, params);
        } else {
            return executeSingleQuery(query, params);
        }
    }

    private List<?> executeListQuery(String query, QueryValidationParams params) {
        if (params.hasNamedParameters()) {
            log.debug("Executing named parameter list query with parameters: {}", params.getNamedParameters());
            return namedParameterJdbcTemplate.queryForList(query, new MapSqlParameterSource(params.getNamedParameters()));
        } else if (params.hasPositionalParameters()) {
            log.debug("Executing positional parameter list query with parameters: {}", params.getPositionalParameters());
            return jdbcTemplate.queryForList(query, params.getPositionalParameters().toArray());
        } else {
            log.debug("Executing list query without parameters");
            return jdbcTemplate.queryForList(query);
        }
    }

    private Object executeSingleQuery(String query, QueryValidationParams params) {
        try {
            if (params.hasNamedParameters()) {
                log.debug("Executing named parameter single query with parameters: {}", params.getNamedParameters());
                return namedParameterJdbcTemplate.queryForObject(query, new MapSqlParameterSource(params.getNamedParameters()), Object.class);
            } else if (params.hasPositionalParameters()) {
                log.debug("Executing positional parameter single query with parameters: {}", params.getPositionalParameters());
                return jdbcTemplate.queryForObject(query, params.getPositionalParameters().toArray(), Object.class);
            } else {
                log.debug("Executing single query without parameters");
                return jdbcTemplate.queryForObject(query, Object.class);
            }
        } catch (EmptyResultDataAccessException e) {
            log.debug("Query returned no results");
            return null;
        } catch (IncorrectResultSizeDataAccessException e) {
            // If multiple results but expecting one, return count if specified
            if ("count".equalsIgnoreCase(params.getComparisonType())) {
                log.debug("Multiple results found, returning count as specified by comparison type");
                return getResultCount(query, params);
            }
            log.error("Query returned unexpected number of results", e);
            throw e;
        }
    }

    private int getResultCount(String query, QueryValidationParams params) {
        if (params.hasNamedParameters()) {
            return namedParameterJdbcTemplate.queryForList(query, new MapSqlParameterSource(params.getNamedParameters())).size();
        } else if (params.hasPositionalParameters()) {
            return jdbcTemplate.queryForList(query, params.getPositionalParameters().toArray()).size();
        } else {
            return jdbcTemplate.queryForList(query).size();
        }
    }
}
