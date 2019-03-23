package com.github.echisan.wbp4j.http;

import java.util.Map;

/**
 * http请求响应封装
 */
public interface WbpHttpResponse {
    int getStatusCode();

    Map<String, String> getHeader();

    String getBody();
}
