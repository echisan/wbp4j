package com.github.echisan.wbp4j.interceptor;

import com.github.echisan.wbp4j.UploadAttributes;
import com.github.echisan.wbp4j.UploadResponse;
import com.github.echisan.wbp4j.WbpConstants;
import com.github.echisan.wbp4j.cache.AbstractCookieContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 检查是否存在cookie，并且设置cookie绑定到上传参数当中去
 */
public class CookieInterceptor implements UploadInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(CookieInterceptor.class);

    /**
     * cookie会话
     */
    private AbstractCookieContext cookieContext;

    public CookieInterceptor(AbstractCookieContext cookieContext) {
        this.cookieContext = cookieContext;
    }

    /**
     * 从会话中获取cookie
     * 如果存在则将值绑定到参数中
     * 不存在则通知com.github.echisan.wbp4j.interceptor.LoginInterceptor拦截器去获取cookie
     *
     * @param uploadAttributes 上传图片的参数
     * @return 如果返回false则说明无法操作cookie缓存
     */
    @Override
    public boolean processBefore(UploadAttributes uploadAttributes) {

        String cookie = null;
        try {
            cookie = cookieContext.getCookie();
        } catch (IOException e) {
            e.printStackTrace();
            uploadAttributes.getContext().put(WbpConstants.UA_ERROR_MESSAGE, "无法访问缓存，获取Cookie失败，上传失败");
            return false;
        }

        if (cookie != null) {
            // 设置cookie到请求头中
            uploadAttributes.getHeaders().put("Cookie", cookie);
        } else {
            // 通知
            uploadAttributes.getContext().put(WbpConstants.REQUIRE_LOGIN, Boolean.TRUE);
        }
        return true;
    }

    @Override
    public void processAfter(UploadResponse uploadResponse) {

    }
}
