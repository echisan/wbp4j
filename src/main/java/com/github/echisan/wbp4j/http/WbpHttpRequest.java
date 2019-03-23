package com.github.echisan.wbp4j.http;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * http请求接口封装
 */
public interface WbpHttpRequest {
    void setHeader(Map<String, String> header);

    Map<String, String> getHeader();

    WbpHttpResponse doGet(String url, Map<String, String> header, Map<String, String> params) throws IOException;

    WbpHttpResponse doGet(String url, Map<String, String> params) throws IOException;

    WbpHttpResponse doGet(String url) throws IOException;

    WbpHttpResponse doPost(String url, Map<String, String> header, Map<String, String> params) throws IOException;

    WbpHttpResponse doPost(String url, Map<String, String> params) throws IOException;

    WbpHttpResponse doPost(String url) throws IOException;

    WbpHttpResponse doPostMultiPart(String url, Map<String, String> header, String content) throws IOException;

    default String convertParams(Map<String, String> params) {
        Set<Map.Entry<String, String>> entries = params.entrySet();
        StringBuilder sb = new StringBuilder();
        entries.forEach(e -> {
            sb.append(e.getKey());
            sb.append("=");
            sb.append(e.getValue());
            sb.append("&");
        });
        String s = sb.toString();
        return s.substring(0, s.length() - 1);
    }
}
