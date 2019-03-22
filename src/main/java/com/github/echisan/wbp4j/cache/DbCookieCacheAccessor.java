package com.github.echisan.wbp4j.cache;

/**
 * 假如有人想把cookie缓存到数据里也不是不行
 * 可以继承本来实现saveCookie方法与getCookie方法
 * 或者直接实现CookieCacheAccessor借口即可
 *
 * 如果需要id啥的还不知道怎么整
 */
public abstract class DbCookieCacheAccessor implements CookieCacheAccessor {

}
