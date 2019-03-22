package com.github.echisan.wbp4j.cache;

import java.io.IOException;

/**
 * 一个用于访问cookie持久层的接口
 */
public interface CookieCacheAccessor {

    void saveCookie(String cookie) throws IOException;

    String getCookie() throws IOException;
}
