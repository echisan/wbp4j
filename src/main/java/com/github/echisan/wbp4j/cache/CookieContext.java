package com.github.echisan.wbp4j.cache;

import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * cookie缓存的上下文
 * 负责管理内存中的cookie以及持久层中的cookie
 */
public class CookieContext extends AbstractCookieContext {
    private static final Logger logger = Logger.getLogger(CookieContext.class);

    @Override
    public String getCookie() throws IOException {
        String cookie = CookieHolder.getCookie();
        if (cookie == null) {

            logger.debug("[ wbp4j - CookieContext ] cannot find cookie in memory, read from persistence cache.");
            cookie = accessor.getCookie();
        }
        if (cookie == null) {

            logger.debug("[ wbp4j - CookieContext ] cannot find cookie in persistence cache.");

            return null;
        }

        CookieHolder.setCookie(cookie);

        return cookie;
    }

    @Override
    public void setCookie(String cookie) throws IOException {
        CookieHolder.setCookie(cookie);
        accessor.saveCookie(cookie);
    }
}
