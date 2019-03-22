package com.github.echisan.wbp4j.interceptor;

import com.github.echisan.wbp4j.UploadResponse;
import com.github.echisan.wbp4j.exception.LoginFailedException;
import com.github.echisan.wbp4j.UploadAttributes;

import java.io.IOException;

/**
 * 拦截器接口
 */
public interface UploadInterceptor {

    boolean processBefore(UploadAttributes uploadAttributes) throws IOException, LoginFailedException;

    boolean processAfter(UploadResponse uploadResponse);

}
