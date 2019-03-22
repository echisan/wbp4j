package com.github.echisan.wbp4j.interceptor;

import com.github.echisan.wbp4j.LoginRequest;
import com.github.echisan.wbp4j.UploadAttributes;
import com.github.echisan.wbp4j.UploadResponse;
import com.github.echisan.wbp4j.WbpConstants;
import com.github.echisan.wbp4j.exception.LoginFailedException;

/**
 * 登陆的拦截器
 * <p>
 * 为了与上传解耦，将登陆的操作放在拦截器
 * <p>
 * 目前拦截器顺序 CookieInterceptor --> LoginInterceptor
 * 目的是上传之前必须存在cookie调用上传接口才有意义
 * 所以本拦截器主要作用在于通过登陆获取cookie
 */
public class LoginInterceptor implements UploadInterceptor {

    /**
     * 登陆操作的接口
     */
    private LoginRequest loginRequest;

    public LoginInterceptor(LoginRequest loginRequest) {
        this.loginRequest = loginRequest;
    }

    @Override
    public boolean processBefore(UploadAttributes uploadAttributes) {

        if (requireLogin(uploadAttributes)) {
            try {
                loginRequest.login();
            } catch (LoginFailedException e) {
                e.printStackTrace();
                uploadAttributes.getContext().put(WbpConstants.UA_ERROR_MESSAGE,"登陆失败，上传图片失败");
                return false;
            }
        }
        return true;
    }

    @Override
    public void processAfter(UploadResponse uploadResponse) {

    }

    private boolean requireLogin(UploadAttributes uploadAttributes) {
        return uploadAttributes.getContext().containsKey(WbpConstants.REQUIRE_LOGIN);
    }
}
