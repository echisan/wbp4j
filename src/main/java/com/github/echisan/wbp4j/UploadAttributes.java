package com.github.echisan.wbp4j;

import java.util.Map;

public class UploadAttributes {

    private String url;
    private Map<String, String> headers;
    private String base64;

    private Map<Object, Object> context;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    public Map<Object, Object> getContext() {
        return context;
    }

    public void setContext(Map<Object, Object> context) {
        this.context = context;
    }
}
