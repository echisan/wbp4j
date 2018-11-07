package com.github.echisan.wbp4j.io;

import java.io.IOException;

public interface CookieCacheable {

    void saveCookie(String cookie) throws IOException;

    String readCookie() throws IOException;

}
