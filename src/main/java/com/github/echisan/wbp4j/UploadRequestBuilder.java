package com.github.echisan.wbp4j;

import com.github.echisan.wbp4j.cache.AbstractCookieContext;
import com.github.echisan.wbp4j.cache.CookieCacheAccessor;
import com.github.echisan.wbp4j.cache.CookieContext;
import com.github.echisan.wbp4j.cache.FileCookieCacheAccessor;
import com.github.echisan.wbp4j.http.DefaultWbpHttpRequest;
import com.github.echisan.wbp4j.http.WbpHttpRequest;
import com.github.echisan.wbp4j.interceptor.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于构建一个UploadRequest的类
 * 由于需要多个组建进行配合，组装起来稍有复杂，所以提供一个类简化组装操作
 */
public class UploadRequestBuilder {

    public static UploadRequest buildDefault(String username, String password) {
        return new Builder(username, password).build();
    }

    public static Builder custom(String username, String password) {
        return new Builder(username, password);
    }

    static class Builder {
        private CookieCacheAccessor cookieCacheAccessor = new FileCookieCacheAccessor();
        private AbstractCookieContext abstractCookieContext = new CookieContext(cookieCacheAccessor);
        private List<UploadInterceptor> uploadInterceptors = new ArrayList<>();
        private AbstractLoginRequest loginRequest = new WbpLoginRequest(abstractCookieContext);
        private WbpHttpRequest wbpHttpRequest = new DefaultWbpHttpRequest();
        private String username;
        private String password;
        private boolean retryable = true;
        private RetryableUploadRequest retryableUploadRequest;

        public Builder(String username, String password) {
            this.username = username;
            this.password = password;
            // init default interceptors
            InitUploadAttributesInterceptor initUploadAttributesInterceptor =
                    new InitUploadAttributesInterceptor();
            CookieInterceptor cookieInterceptor = new CookieInterceptor(abstractCookieContext);
            LoginInterceptor loginInterceptor = new LoginInterceptor(loginRequest);
            loginRequest.setUsernamePassword(username, password);
            ReCheckCookieInterceptor reCheckCookieInterceptor = new ReCheckCookieInterceptor(abstractCookieContext);

            uploadInterceptors.add(initUploadAttributesInterceptor);
            uploadInterceptors.add(cookieInterceptor);
            uploadInterceptors.add(loginInterceptor);
            uploadInterceptors.add(reCheckCookieInterceptor);
        }

        public Builder setAbstractCookieContext(AbstractCookieContext abstractCookieContext) {
            this.abstractCookieContext = abstractCookieContext;
            return this;
        }

        public Builder setUploadInterceptors(List<UploadInterceptor> uploadInterceptors) {
            this.uploadInterceptors = uploadInterceptors;
            return this;
        }

        public Builder addUploadInterceptor(UploadInterceptor uploadInterceptor) {
            this.uploadInterceptors.add(uploadInterceptor);
            return this;
        }

        public Builder addUploadInterceptor(int index, UploadInterceptor uploadInterceptor) {
            this.uploadInterceptors.add(index, uploadInterceptor);
            return this;
        }

        public Builder addUploadInterceptors(List<UploadInterceptor> uploadInterceptors) {
            this.uploadInterceptors.addAll(uploadInterceptors);
            return this;
        }

        public Builder setLoginRequest(AbstractLoginRequest loginRequest) {
            this.loginRequest = loginRequest;
            return this;
        }

        public Builder setUsernamePassword(String username, String password) {
            this.username = username;
            this.password = password;
            return this;
        }

        public Builder setRetryable(boolean retryable) {
            this.retryable = retryable;
            return this;
        }

        public Builder setCacheFilename(String filename) {
            this.cookieCacheAccessor = new FileCookieCacheAccessor(filename);
            this.abstractCookieContext.setAccessor(cookieCacheAccessor);
            return this;
        }

        public UploadRequest build() {
            if (username == null || password == null) {
                throw new IllegalArgumentException("用户名密码不能为空");
            }

            WbpUploadRequest wbpUploadRequest = new WbpUploadRequest(uploadInterceptors, wbpHttpRequest);

            if (retryable) {
                retryableUploadRequest = new DefaultRetryUploadRequest(wbpUploadRequest);
                return retryableUploadRequest;
            }
            return wbpUploadRequest;
        }

        public AbstractCookieContext getAbstractCookieContext() {
            return abstractCookieContext;
        }

        public List<UploadInterceptor> getUploadInterceptors() {
            return uploadInterceptors;
        }

        public LoginRequest getLoginRequest() {
            return loginRequest;
        }

        public WbpHttpRequest getWbpHttpRequest() {
            return wbpHttpRequest;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public boolean isRetryable() {
            return retryable;
        }

        public RetryableUploadRequest getRetryableUploadRequest() {
            return retryableUploadRequest;
        }
    }
}
