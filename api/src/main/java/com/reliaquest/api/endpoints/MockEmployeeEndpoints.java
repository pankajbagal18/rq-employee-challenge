package com.reliaquest.api.endpoints;

public enum MockEmployeeEndpoints {
    ALL_EMPLOYEES("", "GET");
    private String url;
    private String requestType;

    MockEmployeeEndpoints(String url, String requestType) {
        this.url = url;
        this.requestType = requestType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }
}
