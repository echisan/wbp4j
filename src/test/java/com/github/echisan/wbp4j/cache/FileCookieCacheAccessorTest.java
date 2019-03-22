package com.github.echisan.wbp4j.cache;

import org.junit.Test;

import java.io.IOException;

public class FileCookieCacheAccessorTest {
    CookieCacheAccessor cca = new FileCookieCacheAccessor();

    @Test
    public void saveCookie() throws IOException {

        cca.saveCookie("test cookie");
    }

    @Test
    public void getCookie() throws IOException {
        String cookie = cca.getCookie();
        System.out.println(cookie);
    }
}