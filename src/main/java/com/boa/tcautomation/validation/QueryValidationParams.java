package com.boa.tcautomation.validation;

import java.util.List;
import java.util.Map;

public class QueryValidationParams {
    private String query;
    private Object expectedOutput;
    private String comparisonType = "equals"; // Default comparison type
    private boolean ignoreCase = false;
    private Double tolerance = 0.0001; // Default tolerance for numeric comparisons
    private String resultType = "single"; // "single" or "list"
    private Map<String, Object> namedParameters; // For named parameter queries
    private List<Object> positionalParameters; // For positional parameter queries

    // Getters and setters
    public String getQuery() { return query; }
    public void setQuery(String query) { this.query = query; }

    public Object getExpectedOutput() { return expectedOutput; }
    public void setExpectedOutput(Object expectedOutput) { this.expectedOutput = expectedOutput; }

    public String getComparisonType() { return comparisonType; }
    public void setComparisonType(String comparisonType) { this.comparisonType = comparisonType; }

    public boolean isIgnoreCase() { return ignoreCase; }
    public void setIgnoreCase(boolean ignoreCase) { this.ignoreCase = ignoreCase; }

    public Double getTolerance() { return tolerance; }
    public void setTolerance(Double tolerance) { this.tolerance = tolerance; }

    public String getResultType() { return resultType; }
    public void setResultType(String resultType) { this.resultType = resultType; }

    public Map<String, Object> getNamedParameters() { return namedParameters; }
    public void setNamedParameters(Map<String, Object> namedParameters) { this.namedParameters = namedParameters; }

    public List<Object> getPositionalParameters() { return positionalParameters; }
    public void setPositionalParameters(List<Object> positionalParameters) { this.positionalParameters = positionalParameters; }

    public boolean hasNamedParameters() { return namedParameters != null && !namedParameters.isEmpty(); }
    public boolean hasPositionalParameters() { return positionalParameters != null && !positionalParameters.isEmpty(); }
}
