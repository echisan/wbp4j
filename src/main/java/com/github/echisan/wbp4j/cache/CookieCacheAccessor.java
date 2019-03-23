package com.github.echisan.wbp4j.cache;

import java.io.IOException;

/**
 * 一个用于访问cookie持久层的接口
 * 定义了两个接口，上层调用着只需关注获取以及保存两个主要功能
 * 而无需在意是从数据库获取还是从文件中获取
 */
public interface CookieCacheAccessor {

    /**
     * 保存cookie到持久层介质中
     *
     * @param cookie cookie
     * @throws IOException ioe
     */
    void saveCookie(String cookie) throws IOException;

    String getCookie() throws IOException;
}
