package com.github.echisan.wbp4j.cache;

import java.io.IOException;

/**
 * 假如有人想把cookie缓存到数据库里也不是不行
 * 可以继承本类实现saveCookie方法与getCookie方法
 * 或者直接实现CookieCacheAccessor借口也可
 * <p>
 * 因为上层调用者只关心获取而不关心如何获取
 * 需要什么参数进行获取，比如在数据库中可能需要根据id获取响应的cookie
 * 即 getCookie(int id)
 * 但接口只有一个空参数的方法，所以获取哪一个cookie需要根据自己的策略去调整
 * 尽量只暴露getCookie()方法
 * <p>
 * 可以在子类中添加供向变量，但一定要确保线程安全
 */
public abstract class DbCookieCacheAccessor implements CookieCacheAccessor {
    public abstract void setCookieToDb(String cookie) throws IOException;

    public abstract String getCookieFromDb() throws IOException;

    @Override
    public void saveCookie(String cookie) throws IOException {
        setCookieToDb(cookie);
    }

    @Override
    public String getCookie() throws IOException {
        return getCookieFromDb();
    }
}
