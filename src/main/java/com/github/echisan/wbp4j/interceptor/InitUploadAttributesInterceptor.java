package com.github.echisan.wbp4j.interceptor;

import com.github.echisan.wbp4j.UploadAttributes;
import com.github.echisan.wbp4j.UploadResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * 绑定上传参数的拦截器
 * 应该作为首个拦截器
 */
public class InitUploadAttributesInterceptor implements UploadInterceptor {

    private static final String uploadUrl = "http://picupload.service.weibo.com/interface/pic_upload.php?" +
            "ori=1&mime=image%2Fjpeg&data=base64&url=0&markpos=1&logo=&nick=0&marks=1&app=miniblog";

    /**
     * 上传图片的请求头
     * 已经提供了默认的参数，也可以通过构造器进行覆盖
     */
    private Map<String, String> headers;

    public InitUploadAttributesInterceptor() {
        headers = initUploadRequestHeaders();
    }

    public InitUploadAttributesInterceptor(Map<String, String> headers) {
        this.headers = headers;
    }

    @Override
    public boolean processBefore(UploadAttributes uploadAttributes) {

        uploadAttributes.setHeaders(headers);
        uploadAttributes.setUrl(uploadUrl);
        return true;
    }

    @Override
    public void processAfter(UploadResponse uploadResponse) {

    }

    private Map<String, String> initUploadRequestHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Host", "picupload.service.weibo.com");
        headers.put("Origin", "https://weibo.com/");
        headers.put("Referer", "https://weibo.com/");
        return headers;
    }
}
