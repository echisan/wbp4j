package cn.echisan.wbp4j.utils;

import org.apache.http.Header;

import java.util.Arrays;


/**
 * Created by echisan on 2018/6/13
 */
public class WbpResponse {

    private int statusCode;
    private Header[] headers;
    private String body;

    public Header[] getHeaders() {
        return headers;
    }

    public void setHeaders(Header[] headers) {
        this.headers = headers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public String toString() {
        return "WbpResponse{" +
                "statusCode=" + statusCode +
                ", headers=" + Arrays.toString(headers) +
                ", body='" + body + '\'' +
                '}';
    }
}
