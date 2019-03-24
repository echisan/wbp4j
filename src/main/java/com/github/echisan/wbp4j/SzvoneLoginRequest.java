package com.github.echisan.wbp4j;

import com.alibaba.fastjson.JSON;
import com.github.echisan.wbp4j.cache.AbstractCookieContext;
import com.github.echisan.wbp4j.exception.LoginFailedException;
import com.github.echisan.wbp4j.http.DefaultWbpHttpRequest;
import com.github.echisan.wbp4j.http.WbpHttpRequest;
import com.github.echisan.wbp4j.http.WbpHttpResponse;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.net.HttpURLConnection.HTTP_OK;

/**
 * 另一种登陆方案
 * 来自 @szvone: https://github.com/szvone/imgApi
 */
public class SzvoneLoginRequest extends AbstractLoginRequest {
    private static final Logger logger = LoggerFactory.getLogger(SzvoneLoginRequest.class);
    private static final String loginUrl = "https://login.sina.com.cn/sso/login.php?client=ssologin.js(v1.4.15)&_=";

    private WbpHttpRequest wbpHttpRequest;

    private AbstractCookieContext cookieContext;

    public SzvoneLoginRequest(WbpHttpRequest wbpHttpRequest, AbstractCookieContext cookieContext) {
        this.wbpHttpRequest = wbpHttpRequest;
        this.cookieContext = cookieContext;
    }

    public SzvoneLoginRequest(AbstractCookieContext cookieContext) {
        this(new DefaultWbpHttpRequest(), cookieContext);
    }

    @Override
    public void login() throws LoginFailedException {

        try {
            WbpHttpResponse wbpHttpResponse = wbpHttpRequest.doPost(loginUrl, getDefaultLoginHeader(), getLoginParams());

            if (wbpHttpResponse.getStatusCode() != HTTP_OK) {
                throw new LoginFailedException("登陆失败，响应状态码为：" + wbpHttpResponse.getStatusCode());
            }

            logger.debug("responseHeader:" + wbpHttpResponse.getHeader());
            logger.debug("responseBody:" + wbpHttpResponse.getBody());

            LoginResponseEntity loginResponseEntity = JSON.parseObject(wbpHttpResponse.getBody(), LoginResponseEntity.class);

            if (!loginResponseEntity.retcode.equals("0")) {
                throw new LoginFailedException("登陆失败，原因未知。" + wbpHttpResponse.getBody());
            }

            String cookieFromHeaders = getCookieFromHeaders(wbpHttpResponse.getHeader());
            cookieContext.setCookie(cookieFromHeaders);

        } catch (IOException e) {
            e.printStackTrace();
            throw new LoginFailedException("登陆失败，无法发送请求。");
        }

    }

    private Map<String, String> getLoginParams() {
        Map<String, String> params = new HashMap<>();
        params.put("entry", "sso");
        params.put("gateway", "1");
        params.put("from", "null");
        params.put("savestate", "30");
        params.put("useticket", "0");
        params.put("pagerefer", "");
        params.put("vsnf", "1");
        params.put("su", Base64.encode(getUsername().getBytes()));
        params.put("service", "sso");
        params.put("sp", getPassword());
        params.put("sr", "1024*768");
        params.put("encoding", "UTF-8");
        params.put("cdult", "3");
        params.put("domain", "sina.com.cn");
        params.put("prelt", "0");
        params.put("returntype", "TEXT");
        return params;
    }


    public static class LoginResponseEntity {
        private String retcode;

        private String uid;

        private String nick;

        private List<String> crossDomainUrlList;

        public void setRetcode(String retcode) {
            this.retcode = retcode;
        }

        public String getRetcode() {
            return this.retcode;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getUid() {
            return this.uid;
        }

        public void setNick(String nick) {
            this.nick = nick;
        }

        public String getNick() {
            return this.nick;
        }

        public void setString(List<String> crossDomainUrlList) {
            this.crossDomainUrlList = crossDomainUrlList;
        }

        public List<String> getString() {
            return this.crossDomainUrlList;
        }

    }

    private String getCookieFromHeaders(Map<String, String> headers) throws LoginFailedException {
        String str = headers.get("Set-Cookie");
        if (str == null) {
            throw new LoginFailedException("登陆失败，没有返回cookie");
        }

        if (str.contains("SUB=")) {
            String[] split = str.split(";");
            String cookie = null;
            for (String s : split) {
                if (s.contains("SUB=")) {
                    cookie = s.trim();
                    break;
                }
            }
            return cookie;
        }
        logger.error(str);
        throw new LoginFailedException("登陆失败，未获取到必须的cookie字段。");
    }
}


