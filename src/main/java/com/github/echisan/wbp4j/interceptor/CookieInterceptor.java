package com.github.echisan.wbp4j.interceptor;

import com.github.echisan.wbp4j.UploadResponse;
import com.github.echisan.wbp4j.WbpConstants;
import com.github.echisan.wbp4j.cache.AbstractCookieContext;
import com.github.echisan.wbp4j.UploadAttributes;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * 为每一个上传请求添加cookie请求头
 */
public class CookieInterceptor implements UploadInterceptor {
    private static final Logger logger = Logger.getLogger(CookieInterceptor.class);

    private AbstractCookieContext cookieContext;

    public CookieInterceptor(AbstractCookieContext cookieContext) {
        this.cookieContext = cookieContext;
    }

    @Override
    public boolean processBefore(UploadAttributes uploadAttributes) throws IOException {

        String cookie = cookieContext.getCookie();

        if (cookie!=null){
            uploadAttributes.getHeaders().put("Cookie", cookie);
        }else {
            uploadAttributes.getContext().put(WbpConstants.REQUIRE_LOGIN,Boolean.TRUE);
        }
        return true;
    }

    @Override
    public boolean processAfter(UploadResponse uploadResponse) {
        return true;
    }
}
