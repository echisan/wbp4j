package com.github.echisan.wbp4j.interceptor;

import com.github.echisan.wbp4j.UploadResponse;
import com.github.echisan.wbp4j.exception.LoginFailedException;
import com.github.echisan.wbp4j.UploadAttributes;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class InitUploadAttributesInterceptor implements UploadInterceptor {

    private final String uploadUrl = "http://picupload.service.weibo.com/interface/pic_upload.php?" +
            "ori=1&mime=image%2Fjpeg&data=base64&url=0&markpos=1&logo=&nick=0&marks=1&app=miniblog";

    private final Map<String, String> headers = new HashMap<>();

    public InitUploadAttributesInterceptor() {
        initUploadRequestHeaders();
    }

    @Override
    public boolean processBefore(UploadAttributes uploadAttributes) throws IOException, LoginFailedException {

        uploadAttributes.setHeaders(headers);
        uploadAttributes.setUrl(uploadUrl);
        return true;
    }

    @Override
    public boolean processAfter(UploadResponse uploadResponse) {
        return true;
    }


    private Map<String, String> initUploadRequestHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Host", "picupload.service.weibo.com");
        headers.put("Origin", "https://weibo.com/");
        headers.put("Referer", "https://weibo.com/");
        return headers;
    }
}
