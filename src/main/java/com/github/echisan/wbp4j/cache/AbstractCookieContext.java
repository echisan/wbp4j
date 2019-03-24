package com.github.echisan.wbp4j.cache;

import java.io.IOException;

/**
 * 提供统一的获取cookie接口
 * 负责管理内存中的cookie以及缓存文件中的cookie
 * 无需上层调用者知道具体实现逻辑
 * 只需要简单的get与set即可
 */
public abstract class AbstractCookieContext {

    /**
     * 持久层缓存的接口
     */
    protected final CookieCacheAccessor accessor;

    public AbstractCookieContext(CookieCacheAccessor accessor) {
        if (accessor != null) {
            this.accessor = accessor;
        } else {
            this.accessor = new FileCookieCacheAccessor();
        }
    }

    public AbstractCookieContext() {
        this(new FileCookieCacheAccessor());
    }

    public abstract void setCookie(String cookie) throws IOException;

    public abstract String getCookie() throws IOException;

    public CookieCacheAccessor getAccessor() {
        return accessor;
    }

}
