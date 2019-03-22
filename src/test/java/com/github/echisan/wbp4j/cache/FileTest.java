package com.github.echisan.wbp4j.cache;

import org.junit.Test;

import java.io.IOException;

public class FileTest {

    @Test
    public void write() throws IOException {
        String str = "test_write2";
        CookieCacheAccessor accessor = new FileCookieCacheAccessor();

        accessor.saveCookie(str);

    }
}
