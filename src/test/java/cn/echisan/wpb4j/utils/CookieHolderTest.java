package cn.echisan.wpb4j.utils;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class CookieHolderTest {

    @Test
    public void setCookies() throws IOException {
        CookieHolder.setCookies("set cookies String","newCookieFile");
        String cookies = CookieHolder.getCookies();
        System.out.println(cookies);
    }

    @Test
    public void getCookies() throws IOException {
        String cookies = CookieHolder.getCookies();
        System.out.println(cookies);
    }

    @Test
    public void getCookiesFilePath() {
        String cookiesPath = CookieHolder.getCookiesPath();
        System.out.println(cookiesPath);
    }
}