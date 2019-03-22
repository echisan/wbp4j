package com.github.echisan.wbp4j;

import com.alibaba.fastjson.JSON;
import com.github.echisan.wbp4j.Entity.PreLogin;
import com.github.echisan.wbp4j.cache.AbstractCookieContext;
import com.github.echisan.wbp4j.exception.LoginFailedException;
import com.github.echisan.wbp4j.http.WbpHttpRequest;
import com.github.echisan.wbp4j.http.WbpHttpResponse;
import com.github.echisan.wbp4j.utils.RSAEncodeUtils;
import org.apache.log4j.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static java.net.HttpURLConnection.HTTP_OK;

public class WbpLoginRequest implements LoginRequest {
    private static final Logger logger = Logger.getLogger(WbpLoginRequest.class);
    private static String preLoginUrl = "https://login.sina.com.cn/sso/prelogin.php";
    private static String loginUrl = "https://login.sina.com.cn/sso/login.php?client=ssologin.js(v1.4.19)";
    private static String _username;
    private static String _password;
    private final Map<String, String> preLoginHeaders;
    private final Map<String, String> preLoginParams;
    private final Map<String, String> loginHeaders;

    /**
     * HttpRequest接口
     * 默认使用DefaultWbpHttpRequest
     * 也可以自己采用HttpClient实现该接口
     */
    private WbpHttpRequest wbpHttpRequest;

    private AbstractCookieContext cookieContext;

    public WbpLoginRequest(WbpHttpRequest wbpHttpRequest, AbstractCookieContext cookieContext) {
        this.wbpHttpRequest = wbpHttpRequest;
        this.cookieContext = cookieContext;

        this.preLoginHeaders = getDefaultPreLoginHeader();
        this.preLoginParams = getDefaultPreLoginParams();
        this.loginHeaders = getDefaultLoginHeader();
    }

    public WbpLoginRequest(Map<String, String> preLoginHeaders,
                           Map<String, String> preLoginParams,
                           Map<String, String> loginHeaders,
                           WbpHttpRequest wbpHttpRequest) {

        this.preLoginHeaders = preLoginHeaders;
        this.preLoginParams = preLoginParams;
        this.loginHeaders = loginHeaders;
        this.wbpHttpRequest = wbpHttpRequest;
    }

    public void setAccount(String username, String password) {
        if (username == null || password == null) {
            throw new IllegalArgumentException("username or password cannot be null.");
        }
        _username = username;
        _password = password;
    }

    private PreLogin preLogin() throws LoginFailedException {

        if (!checkAccount()) {
            throw new LoginFailedException("username or password cannot be null.");
        }

        // put username to pre login params map.
        setUsernameToPreLoginParams();

        logger.debug("[ preLogin debug info ] \n" +
                "[ preLoginUrl ] " + preLoginUrl +
                "\n[ preLogin headers ] " + preLoginHeaders +
                "\n[ preLogin params ] " + preLoginParams
        );

        try {
            WbpHttpResponse response = wbpHttpRequest.doGet(preLoginUrl, preLoginHeaders, preLoginParams);

            if (response.getStatusCode() != HTTP_OK) {
                throw new LoginFailedException(
                        "pre login failed.\n" +
                                "[ response code ] " + response.getStatusCode() +
                                "\n[ response headers ]" + response.getHeader() +
                                "\n[ response body ] " + response.getBody()
                );
            }

            return JSON.parseObject(response.getBody(), PreLogin.class);

        } catch (IOException e) {
            e.printStackTrace();
            throw new LoginFailedException("pre login failed!! message: [" + e.getMessage() + "]");
        }
    }

    @Override
    public void login() throws LoginFailedException {

        PreLogin preLogin = preLogin();

        Map<String, String> loginParams = createLoginParams(preLogin);

        try {
            WbpHttpResponse response = wbpHttpRequest.doPost(loginUrl, loginHeaders, loginParams);

            if (response.getStatusCode() != HTTP_OK) {
                throw new LoginFailedException(createWbpLoginExceptionMessage("do request login url failed", response));
            }

            // get cookie from response headers
            Map<String, String> header = response.getHeader();
            String cookie = header.get("set-cookie");
            if (cookie == null) {
                cookie = header.get("Set-Cookie");
            }

            if (cookie == null) {

                String reason = getReason(response.getBody());

                if (reason != null) {
                    throw new LoginFailedException("login failed,reason:[ " + reason + " ]");
                } else {
                    throw new LoginFailedException(createWbpLoginExceptionMessage("cannot find set-cookie in headers", response));
                }
            }

            if (cookie.length() < WbpConstants.COOKIE_LENGTH_THRESHOLD) {
                throw new LoginFailedException(createWbpLoginExceptionMessage("cookie is invalid", response));
            }

            logger.info("[ wbp4j ] login success..");
            logger.debug("[ wbp4j - cookie - debug ] " + cookie);
            cookieContext.setCookie(cookie);


        } catch (IOException e) {
            e.printStackTrace();
            throw new LoginFailedException("login failed. message: " + e.getMessage());
        }

    }

    private Map<String, String> getDefaultPreLoginHeader() {
        Map<String, String> header = new HashMap<>();
        header.put("Referer", "https://weibo.com/");
        header.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.79 Safari/537.36");
        return header;
    }

    private Map<String, String> getDefaultPreLoginParams() {
        Map<String, String> params = new HashMap<>();
        params.put("client", "ssologin.js(v1.4.19)");
        params.put("entry", "weibo");
//        params.put("su", _username);
        params.put("rsakt", "mod");
        params.put("checkpin", "1");
        params.put("_", String.valueOf(System.currentTimeMillis()));
        return params;
    }

    private void setUsernameToPreLoginParams() {
        if (!preLoginParams.containsKey("su")) {
            preLoginParams.put("su", _username);
        }
    }

    private String createWbpLoginExceptionMessage(String message, WbpHttpResponse response) {
        return "[ login failed message ]" + message +
                "\n[ response code ] " + response.getStatusCode() +
                "\n[ response headers ]" + response.getHeader() +
                "\n[ response body ]" + response.getBody();
    }


    private Map<String, String> getDefaultLoginHeader() {
        Map<String, String> header = new HashMap<>();
        header.put("Referer", "https://weibo.com/");
        header.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.79 Safari/537.36\"");
        header.put("Content-Type", "application/x-www-form-urlencoded");
        header.put("Accept", "text/html,application/xhtml+xm…plication/xml;q=0.9,*/*;q=0.8");
        header.put("Accept-Encoding", "deflate, br");
        header.put("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
        return header;
    }

    private boolean checkAccount() {
        return _username != null && _password != null;
    }

    private Map<String, String> createLoginParams(PreLogin preLogin) throws LoginFailedException {
        // 根据微博加密js中密码拼接的方法
        String pwd = preLogin.getServertime() + "\t" + preLogin.getNonce() + "\n" + _password;

        Map<String, String> params = new HashMap<>();
        params.put("encoding", "UTF-8");
        params.put("entry", "weibo");
        params.put("from", "");
        params.put("gateway", "1");
        params.put("nonce", preLogin.getNonce());
        params.put("pagerefer", "https://login.sina.com.cn/crossdomain2.php?action=logout&r=https%3A%2F%2Fweibo.com%2Flogout.php%3Fbackurl%3D%252F");
        params.put("prelt", "76");
        params.put("pwencode", "rsa2");
        params.put("qrcode_flag", "false");
        params.put("returntype", "META");
        params.put("rsakv", preLogin.getRsakv());
        params.put("savestate", "7");
        params.put("servertime", String.valueOf(preLogin.getServertime()));
        params.put("service", "miniblog");
        try {
            params.put("sp", RSAEncodeUtils.encode(pwd, preLogin.getPubkey(), "10001"));
            logger.info("login...encrypt password success!");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException |
                InvalidKeySpecException | InvalidKeyException |
                IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
            throw new LoginFailedException("login failed. encrypt password failed. message: " + e.getMessage());
        }
        params.put("sr", "1920*1080");
        params.put("su", Base64.getEncoder().encodeToString(_username.getBytes()));
        params.put("url", "https://weibo.com/ajaxlogin.php?framelogin=1&callback=parent.sinaSSOController.feedBackUrlCallBack");
        params.put("useticket", "1");
        params.put("vsnf", "1");
        return params;
    }


    // parse reason from weibo response html
    private String getReason(String responseHtml) throws UnsupportedEncodingException {

        String location = getLocation(responseHtml);

        if (location == null) {
            return null;
        }

        int i = location.lastIndexOf("&");
        if (i != -1) {
            String substring = location.substring(i);
            String replace = substring.replace("&reason=", "");
            return URLDecoder.decode(replace, "GBK");
        }
        return null;
    }

    private String getLocation(String responseHtml) {
        int replace = responseHtml.indexOf("replace");
        if (replace == -1) {
            return null;
        }
        String substring = responseHtml.substring(replace + 9);
        int i = substring.lastIndexOf(");");
        return substring.substring(0, i - 1);
    }
}
