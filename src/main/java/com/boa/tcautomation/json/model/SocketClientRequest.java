package com.boa.tcautomation.json.model;

import java.util.Map;

public class SocketClientRequest {
    private String host;
    private Map<String, String> params;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }
}
