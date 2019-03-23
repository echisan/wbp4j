package com.github.echisan.wbp4j.cache;

/**
 * 负责管理保存在内存中的cookie缓存
 * <p>
 * 本类必须是线程安全的，cookie可能会被多个线程并发访问
 */
public class CookieHolder {
    private static volatile String _cookie;

    public synchronized static String getCookie() {
        return _cookie;
    }

    public synchronized static void setCookie(String cookie) {
        _cookie = cookie;
    }
}
