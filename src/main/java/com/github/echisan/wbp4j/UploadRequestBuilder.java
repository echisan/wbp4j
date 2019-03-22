package com.github.echisan.wbp4j;

import com.github.echisan.wbp4j.cache.AbstractCookieContext;
import com.github.echisan.wbp4j.cache.CookieContext;
import com.github.echisan.wbp4j.http.DefaultWbpHttpRequest;
import com.github.echisan.wbp4j.http.WbpHttpRequest;
import com.github.echisan.wbp4j.interceptor.CookieInterceptor;
import com.github.echisan.wbp4j.interceptor.InitUploadAttributesInterceptor;
import com.github.echisan.wbp4j.interceptor.LoginInterceptor;
import com.github.echisan.wbp4j.interceptor.UploadInterceptor;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于构建一个UploadRequest的类
 * 由于需要多个组建进行配合，组装起来稍有复杂，所以提供一个类简化组装操作
 */
public class UploadRequestBuilder {

    /**
     * 使用默认配置，只需要用户名密码即可
     *
     * @param username sina username
     * @param password sina password
     * @return uploadRequest
     */
    public static UploadRequest buildDefault(String username, String password) {

        AbstractCookieContext cookieContext = new CookieContext();
        WbpHttpRequest httpRequest = new DefaultWbpHttpRequest();
        List<UploadInterceptor> uploadInterceptors = new ArrayList<>();
        LoginRequest loginRequest = new WbpLoginRequest(httpRequest, cookieContext);

        ((WbpLoginRequest) loginRequest).setAccount(username, password);

        InitUploadAttributesInterceptor initUploadAttributesInterceptor = new InitUploadAttributesInterceptor();
        CookieInterceptor cookieInterceptor = new CookieInterceptor(cookieContext);
        LoginInterceptor loginInterceptor = new LoginInterceptor(loginRequest, cookieContext);

        uploadInterceptors.add(initUploadAttributesInterceptor);
        uploadInterceptors.add(cookieInterceptor);
        uploadInterceptors.add(loginInterceptor);

        return new WbpUploadRequest(uploadInterceptors, httpRequest);
    }

}
