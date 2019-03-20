package com.github.echisan.wbp4j.interceptor;

import com.github.echisan.wbp4j.LoginRequest;
import com.github.echisan.wbp4j.UploadAttributes;
import com.github.echisan.wbp4j.UploadResponse;
import com.github.echisan.wbp4j.WbpConstants;
import com.github.echisan.wbp4j.cache.AbstractCookieContext;
import com.github.echisan.wbp4j.exception.LoginFailedException;

import java.io.IOException;

/**
 * 登陆的拦截器
 * <p>
 * 目前拦截器顺序 CookieInterceptor --> LoginInterceptor
 */
public class LoginInterceptor implements UploadInterceptor {

    private LoginRequest loginRequest;

    private AbstractCookieContext cookieContext;

    public LoginInterceptor(LoginRequest loginRequest, AbstractCookieContext cookieContext) {
        this.loginRequest = loginRequest;
        this.cookieContext = cookieContext;
    }

    @Override
    public boolean processBefore(UploadAttributes uploadAttributes) throws IOException, LoginFailedException {

        if (requireLogin(uploadAttributes)) {
            loginRequest.login();
            uploadAttributes.getHeaders().put("Cookie", cookieContext.getCookie());
        }
        return true;
    }

    @Override
    public boolean processAfter(UploadResponse uploadResponse) {
        return true;
    }


    private boolean requireLogin(UploadAttributes uploadAttributes) {
        return uploadAttributes.getContext().containsKey(WbpConstants.REQUIRE_LOGIN);
    }
}
