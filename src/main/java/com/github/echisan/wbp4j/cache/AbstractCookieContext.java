package com.github.echisan.wbp4j.cache;

import java.io.IOException;

public abstract class AbstractCookieContext {

    protected CookieCacheAccessor accessor;

    public AbstractCookieContext(CookieCacheAccessor accessor) {
        this.accessor = accessor;
    }

    public AbstractCookieContext() {
        this(new FileCookieCacheAccessor());
    }

    public abstract void setCookie(String cookie) throws IOException;

    public abstract String getCookie() throws IOException;
}
