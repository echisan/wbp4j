package com.github.echisan.wbp4j.interceptor;

import com.github.echisan.wbp4j.UploadAttributes;
import com.github.echisan.wbp4j.UploadResponse;
import com.github.echisan.wbp4j.WbpConstants;
import com.github.echisan.wbp4j.cache.AbstractCookieContext;

import java.io.IOException;

/**
 * 二次验证cookie是否存在的拦截器
 * 由于当CookieInterceptor不存在cookie时，交由下一个拦截器去获取
 * 所以上传参数中仍然不存在cookie
 * 所以用本拦截器于进行二次检查
 */
public class ReCheckCookieInterceptor implements UploadInterceptor {

    /**
     * cookie会话
     */
    private AbstractCookieContext cookieContext;

    public ReCheckCookieInterceptor(AbstractCookieContext cookieContext) {
        this.cookieContext = cookieContext;
    }

    @Override
    public boolean processBefore(UploadAttributes uploadAttributes) {

        if (uploadAttributes.getContext().containsKey(WbpConstants.REQUIRE_LOGIN)) {
            try {
                uploadAttributes.getHeaders().put("Cookie", cookieContext.getCookie());
            } catch (IOException e) {
                e.printStackTrace();
                uploadAttributes.getContext().put(WbpConstants.UA_ERROR_MESSAGE, "无法访问Cookie缓存，上传失败");
                return false;
            }
        }

        return true;
    }

    @Override
    public void processAfter(UploadResponse uploadResponse) {

    }
}
