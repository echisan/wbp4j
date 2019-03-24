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
 * 由于需要多个组建进行配合，组装起来稍有复杂，所以提供一个类完成组装操作
 * <p>
 * 虽然想提供一个类供调用者根据自身需求去更换某个组件
 * 但是想了好久都不知道该怎么去实现
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
        private AbstractLoginRequest loginRequest = new SzvoneLoginRequest(abstractCookieContext);
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

        public Builder addInterceptor(UploadInterceptor uploadInterceptor) {
            this.uploadInterceptors.add(uploadInterceptor);
            return this;
        }

        public Builder addInterceptors(List<UploadInterceptor> uploadInterceptors) {
            this.uploadInterceptors.addAll(uploadInterceptors);
            return this;
        }

        public Builder addInterceptor(int index, UploadInterceptor uploadInterceptor) {
            this.uploadInterceptors.add(index, uploadInterceptor);
            return this;
        }

        public Builder setInterceptors(List<UploadInterceptor> uploadInterceptors) {
            if (uploadInterceptors == null || uploadInterceptors.size() == 0) {
                throw new IllegalArgumentException("拦截器必须不能为空！");
            }
            this.uploadInterceptors = uploadInterceptors;
            return this;
        }

        public Builder isRetryable(boolean retryable) {
            this.retryable = retryable;
            return this;
        }

        public Builder setRetryStrategy(RetryableUploadRequest retryStrategy) {
            this.retryableUploadRequest = retryStrategy;
            return this;
        }

        public Builder setCacheFilename(String filename) {
            this.abstractCookieContext = new CookieContext(new FileCookieCacheAccessor(filename));
            return this;
        }

        public UploadRequest build() {
            if (username == null || password == null) {
                throw new IllegalArgumentException("用户名密码不能为空");
            }

            WbpUploadRequest wbpUploadRequest = new WbpUploadRequest(uploadInterceptors, wbpHttpRequest);

            if (retryable) {
                if (this.retryableUploadRequest == null) {
                    retryableUploadRequest = new DefaultRetryUploadRequest(wbpUploadRequest);
                } else {
                    if (this.retryableUploadRequest.getUploadRequest() == null) {
                        this.retryableUploadRequest.setUploadRequest(wbpUploadRequest);
                    }
                }
                return retryableUploadRequest;
            }
            return wbpUploadRequest;
        }
    }
}
