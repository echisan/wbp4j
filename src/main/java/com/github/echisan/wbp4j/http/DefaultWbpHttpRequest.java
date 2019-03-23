package com.github.echisan.wbp4j.http;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.net.HttpURLConnection.HTTP_OK;

/**
 * com.github.echisan.wbp4j.http.WbpHttpRequest默认实现
 * 采用java.net.HttpURLConnection实现
 * 当初也拿HttpClient实现过，为了减少依赖，后来删了
 * 采用HttpURLConnection实现
 * <p>
 * Created by echisan on 2018/11/5
 */
public class DefaultWbpHttpRequest implements WbpHttpRequest {
    private Map<String, String> header;

    public DefaultWbpHttpRequest() {
    }

    /*
     * 初始化header
     * @param header
     */
    public DefaultWbpHttpRequest(Map<String, String> header) {
        this.header = header;
    }

    @Override
    public WbpHttpResponse doGet(String url) throws IOException {
        return doGet(url, this.header, null);
    }

    @Override
    public WbpHttpResponse doGet(String url, Map<String, String> params) throws IOException {
        return doGet(url, this.header, params);
    }

    @Override
    public WbpHttpResponse doGet(String url, Map<String, String> header, Map<String, String> params) throws IOException {
        if (params != null) {
            url = url + "?" + convertParams(params);
        }

        URL u = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) u.openConnection();
        connection.setRequestMethod("GET");

        if (header != null) {
            header.forEach(connection::setRequestProperty);
        }

        connection.connect();
        return new DefaultWbpHttpResponse(
                connection.getResponseCode(),
                getHeaderFromConnection(connection),
                getBodyFromConnection(connection)
        );
    }


    @Override
    public WbpHttpResponse doPost(String url) throws IOException {
        return doPost(url, null);
    }

    @Override
    public WbpHttpResponse doPost(String url, Map<String, String> params) throws IOException {
        return doPost(url, this.header, params);
    }


    @Override
    public WbpHttpResponse doPost(String url, Map<String, String> header, Map<String, String> params) throws IOException {

        URL u = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) u.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setUseCaches(false);

        if (header != null) {
            header.forEach(connection::setRequestProperty);
        }
        connection.connect();

        if (params != null) {
            String requestBody = convertParams(params);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            bw.write(requestBody);
            bw.flush();
            bw.close();
        }
        return new DefaultWbpHttpResponse(
                connection.getResponseCode(),
                getHeaderFromConnection(connection),
                getBodyFromConnection(connection)
        );
    }


    /*
     * 增添header，新的header将替换旧的
     * @param header
     */
    @Override
    public void setHeader(Map<String, String> header) {
        Set<Map.Entry<String, String>> entries = header.entrySet();
        entries.forEach(stringStringEntry -> {
            this.header.put(stringStringEntry.getKey(), stringStringEntry.getValue());
        });
    }

    @Override
    public WbpHttpResponse doPostMultiPart(String url, Map<String, String> header, String content) throws IOException {
        URL u = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) u.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setUseCaches(false);

        if (header != null) {
            header.forEach(connection::setRequestProperty);
        }
        String END_LINE = "\r\n";
        String TWO = "--";
        String boundary = "===" + System.currentTimeMillis() + "===";
        String contentType = "multipart/form-data; boundary=" + boundary;
        connection.setRequestProperty("Content-Type", contentType);
        StringBuilder bodyBulider = new StringBuilder();
        bodyBulider.append(TWO).append(boundary).append(END_LINE)
                .append("Content-Disposition: form-data; name=\"b64_data\"")
                .append(END_LINE).append(END_LINE)
                .append(content)
                .append(END_LINE)
                .append(TWO).append(boundary).append(TWO);
        connection.connect();

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));

        bw.write(bodyBulider.toString());
        bw.flush();
        bw.close();
        return new DefaultWbpHttpResponse(
                connection.getResponseCode(),
                getHeaderFromConnection(connection),
                getBodyFromConnection(connection)
        );
    }

    @Override
    public Map<String, String> getHeader() {
        return this.header;
    }


    private Map<String, String> getHeaderFromConnection(HttpURLConnection connection) {
        Map<String, List<String>> headerFields = connection.getHeaderFields();

        List<String> list = headerFields.get("Set-Cookie");
        String cookie = null;
        if (list != null) {
            StringBuilder sb = new StringBuilder();
            for (String s : list) {
                int i = s.indexOf(";");
                if (i != -1) {
                    String substring = s.substring(0, i);
                    sb.append(substring).append("; ");
                } else {
                    break;
                }
            }
            cookie = sb.toString();
            if (cookie.length() != 0) {
                cookie = cookie.substring(0, cookie.length() - 2);
            }
        }

        Map<String, String> header = new HashMap<>();
        Set<Map.Entry<String, List<String>>> entries = headerFields.entrySet();
        entries.forEach(e -> {
            StringBuilder sb = new StringBuilder();
            List<String> values = e.getValue();
            for (String s : values) {
                sb.append(s);
            }
            header.put(e.getKey(), sb.toString());
        });
        header.put("Set-Cookie", cookie);
        return header;
    }

    private String getBodyFromConnection(HttpURLConnection connection) throws IOException {
        int responseCode = connection.getResponseCode();

        if (responseCode == HTTP_OK) {
            InputStream inputStream = connection.getInputStream();
            return readInputStream(inputStream);
        } else {
            InputStream errorStream = connection.getErrorStream();
            return readInputStream(errorStream);
        }
    }

    private String readInputStream(InputStream is) throws IOException {
        String str;
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
        while ((str = bufferedReader.readLine()) != null) {
            stringBuilder.append(str);
        }
        return stringBuilder.toString();
    }

//    private String convertParams(Map<String, String> params) {
//        Set<Map.Entry<String, String>> entries = params.entrySet();
//        StringBuilder sb = new StringBuilder();
//        entries.forEach(e -> {
//            sb.append(e.getKey());
//            sb.append("=");
//            sb.append(e.getValue());
//            sb.append("&");
//        });
//        String s = sb.toString();
//        return s.substring(0, s.length() - 1);
//    }
}
