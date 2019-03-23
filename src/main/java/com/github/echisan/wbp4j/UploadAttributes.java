package com.github.echisan.wbp4j;

import java.util.HashMap;
import java.util.Map;

/**
 * 上传图片所需要的的参数
 */
public class UploadAttributes {

    /**
     * 上传图片的url
     */
    private String url;

    /**
     * 调用上传图片接口时所需要的请求头
     * 主要关注点在cookie
     * <p>
     * # 我寻思还是直接new一个了吧
     */
    private Map<String, String> headers = new HashMap<>();

    /**
     * 需要上传的图片的base64
     */
    private String base64;

    /**
     * 上下文
     * 调用上传接口并不需要本信息
     * 主要提供能本字段去供给拦截器通信或叫传参
     * 可以网context中添加信息
     * 让拦截器去判断，去获取相关信息
     */
    private Map<Object, Object> context = new HashMap<>();

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 由于主要需要关注的是请求头中的cookie
     * 为此特意提供一个方法，当然不用也是完全可以的
     * @param cookie cookie
     */
    public void setCookie(String cookie) {
        this.headers.put("Cookie", cookie);
    }

    public String getCookie() {
        return this.headers.get("Cookie");
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void addAllHeaders(Map<String, String> headers) {
        this.headers.putAll(headers);
    }

    /**
     * 因为已经初始化了headers，所以如果调用set的话
     * 需要将初始化的header全部清除
     *
     * @param headers the headers
     */
    public void setHeaders(Map<String, String> headers) {
        this.headers.clear();
        this.headers.putAll(headers);
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
