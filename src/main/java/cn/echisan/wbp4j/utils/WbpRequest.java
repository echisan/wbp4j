package cn.echisan.wbp4j.utils;

import org.apache.http.*;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.Map;

/**
 * Created by echisan on 2018/6/13
 */
public class WbpRequest {

    private static final Logger logger = Logger.getLogger(WbpRequest.class);

    private HttpClientBuilder httpClientBuilder;
    private WbpResponse wbpResponse;

    public WbpRequest() {
        this.httpClientBuilder = HttpClientBuilder.create();
        this.wbpResponse = new WbpResponse();
    }

    public WbpResponse doGet(String url) throws IOException {
        CloseableHttpClient httpClient = httpClientBuilder.build();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeaders(getRequestHeader());
        doRequest(httpClient, httpGet);
        return this.wbpResponse;
    }

    public WbpResponse doPost(String url, String imageBase64) throws IOException {
        CloseableHttpClient httpClient = httpClientBuilder.build();
        HttpPost httpPost = new HttpPost(url);
        // set headers,cookie
        httpPost.setHeader("Host","picupload.service.weibo.com");
        httpPost.setHeader("Cookie",CookieHolder.getCookies());
        httpPost.setHeader("Origin","https://weibo.com/");
        httpPost.setHeader("Referer","https://weibo.com/");

        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        StringBody stringBody = new StringBody(imageBase64, ContentType.MULTIPART_FORM_DATA);
        multipartEntityBuilder.addPart("b64_data",stringBody);
        httpPost.setEntity(multipartEntityBuilder.build());

        doRequest(httpClient,httpPost);
        return this.wbpResponse;

    }

    private void doRequest(CloseableHttpClient httpClient, HttpUriRequest httpRequest) throws IOException {

        CloseableHttpResponse response = httpClient.execute(httpRequest);
        Header[] allHeaders = response.getAllHeaders();
        this.wbpResponse.setHeaders(allHeaders);

        int statusCode = response.getStatusLine().getStatusCode();
        this.wbpResponse.setStatusCode(statusCode);
        logger.info("resultCode:"+this.wbpResponse.getStatusCode());

        HttpEntity responseEntity = response.getEntity();
        if (responseEntity.isStreaming()){
            this.setBody(responseEntity.getContent());
        }

        httpClient.close();
        response.close();

    }


    public WbpResponse doPost(String url, Map<String, Object> params) throws IOException {
        CloseableHttpClient httpClient = httpClientBuilder.build();
        HttpPost httpPost = new HttpPost(url);

        httpPost.setHeaders(getRequestHeader());

        String requestParamsString = MapParamsUtils.toString(params);

        HttpEntity httpEntity = new StringEntity(requestParamsString,ContentType.APPLICATION_FORM_URLENCODED);
        httpPost.setEntity(httpEntity);

        CloseableHttpResponse response = httpClient.execute(httpPost);

        Header[] allHeaders = response.getAllHeaders();
        this.wbpResponse.setHeaders(allHeaders);

        HttpEntity responseEntity = response.getEntity();
        if (responseEntity.isStreaming()){
            setBody(responseEntity.getContent());
        }
        httpClient.close();
        response.close();

        return this.wbpResponse;
    }

    private void setBody(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String temp;
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        while ((temp = br.readLine()) != null) {
            stringBuilder.append(temp);
        }
        this.wbpResponse.setBody(stringBuilder.toString());
    }

    public WbpResponse getWbpResponse() {
        return wbpResponse;
    }

    public Header[] getRequestHeader(){
        return new Header[]{
                new BasicHeader("Referer","https://weibo.com/"),
                new BasicHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.79 Safari/537.36"),
                new BasicHeader("Content-Type", "application/x-www-form-urlencoded"),
                new BasicHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8"),
                new BasicHeader("Accept-Encoding", "gzip, deflate, br"),
                new BasicHeader("Accept-Language","zh-CN,zh;q=0.9")
        };
    }
}
