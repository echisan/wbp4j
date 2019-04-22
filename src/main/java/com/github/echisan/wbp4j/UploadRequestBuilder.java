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

    public static class Builder {
        private CookieCacheAccessor cookieCacheAccessor;
        private AbstractCookieContext abstractCookieContext;
        private List<UploadInterceptor> uploadInterceptors = new ArrayList<>();
        private AbstractLoginRequest loginRequest;
        private WbpHttpRequest wbpHttpRequest = new DefaultWbpHttpRequest();
        private String username;
        private String password;
        private boolean retryable = true;
        private RetryableUploadRequest retryableUploadRequest;
        private String cookieFileName = null;

        public Builder(String username, String password) {
            this.username = username;
            this.password = password;
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
            this.cookieFileName = filename;
            return this;
        }

        public Builder setLoginRequest(AbstractLoginRequest loginRequest){
            if (loginRequest == null){
                throw new NullPointerException("loginRequest cannot be null");
            }
            this.loginRequest = loginRequest;
            return this;
        }

        private void buildCookieCacheAccessor(){
            if (cookieCacheAccessor == null){
                if (cookieFileName != null){
                    cookieCacheAccessor = new FileCookieCacheAccessor(cookieFileName);
                }else {
                    cookieCacheAccessor = new FileCookieCacheAccessor();
                }
            }
        }

        public WbpHttpRequest getWbpHttpRequest(){
            assert wbpHttpRequest != null;
            return wbpHttpRequest;
        }

        public AbstractCookieContext getCookieContext(){
            assert abstractCookieContext != null;
            return abstractCookieContext;
        }

        private void buildCookieContext(){
            assert cookieCacheAccessor != null;
            // build cookieContext finish.
            abstractCookieContext = new CookieContext(cookieCacheAccessor);
        }

        private void buildLoginRequest(){
            if (loginRequest == null){
                loginRequest = new SzvoneLoginRequest(wbpHttpRequest,abstractCookieContext);
            }
            loginRequest.setUsernamePassword(this.username,this.password);
        }

        private void buildInterceptors(){
            InitUploadAttributesInterceptor initInterceptor = new InitUploadAttributesInterceptor();
            CookieInterceptor cookieInterceptor = new CookieInterceptor(abstractCookieContext);
            buildLoginRequest();
            LoginInterceptor loginInterceptor = new LoginInterceptor(loginRequest);
            ReCheckCookieInterceptor recheckInterceptor = new ReCheckCookieInterceptor(abstractCookieContext);
            this.uploadInterceptors.add(initInterceptor);
            this.uploadInterceptors.add(cookieInterceptor);
            this.uploadInterceptors.add(loginInterceptor);
            this.uploadInterceptors.add(recheckInterceptor);
        }


        public UploadRequest build() {
            if (username == null || password == null) {
                throw new IllegalArgumentException("用户名密码不能为空");
            }

            WbpUploadRequest wbpUploadRequest = new WbpUploadRequest(uploadInterceptors, wbpHttpRequest);

            buildCookieCacheAccessor();
            buildCookieContext();
            buildInterceptors();

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
