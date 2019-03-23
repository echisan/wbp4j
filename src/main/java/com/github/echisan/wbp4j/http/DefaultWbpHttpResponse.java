package com.github.echisan.wbp4j.http;

import java.util.Map;

/**
 * Created by echisan on 2018/11/5
 */
public class DefaultWbpHttpResponse implements WbpHttpResponse {
    private int statusCode;
    private Map<String, String> header;
    private String body;

    public DefaultWbpHttpResponse(int statusCode, Map<String, String> header, String body) {
        this.statusCode = statusCode;
        this.header = header;
        this.body = body;
    }

    @Override
    public int getStatusCode() {
        return this.statusCode;
    }


    @Override
    public Map<String, String> getHeader() {
        return this.header;
    }

    @Override
    public String getBody() {
        return this.body;
    }

    @Override
    public String toString() {
        return "DefaultWbpHttpResponse{" +
                "statusCode=" + statusCode +
                ", header=" + header +
                ", body='" + body + '\'' +
                '}';
    }
}
