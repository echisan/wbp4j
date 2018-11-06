package cn.echisan.wbp4j.http;

import java.util.Map;

public interface WbpHttpResponse {
    int getStatusCode();

    Map<String, String> getHeader();

    String getBody();
}
