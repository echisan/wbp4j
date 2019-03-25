package com.github.echisan.wbp4j;

import com.alibaba.fastjson.JSON;
import com.github.echisan.wbp4j.entity.PreLogin;
import com.github.echisan.wbp4j.cache.AbstractCookieContext;
import com.github.echisan.wbp4j.exception.LoginFailedException;
import com.github.echisan.wbp4j.http.DefaultWbpHttpRequest;
import com.github.echisan.wbp4j.http.WbpHttpRequest;
import com.github.echisan.wbp4j.http.WbpHttpResponse;
import com.github.echisan.wbp4j.utils.RSAEncodeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

/**
 * 默认的登陆实现
 */
public class WbpLoginRequest extends AbstractLoginRequest {
    private static final Logger logger = LoggerFactory.getLogger(WbpLoginRequest.class);
    private static final String preLoginUrl = "https://login.sina.com.cn/sso/prelogin.php";
    private static final String loginUrl = "https://login.sina.com.cn/sso/login.php?client=ssologin.js(v1.4.19)";
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

    public WbpLoginRequest(AbstractCookieContext cookieContext) {
        this(new DefaultWbpHttpRequest(), cookieContext);
    }

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

    private PreLogin preLogin() throws LoginFailedException {

        if (!checkAccount()) {
            throw new LoginFailedException("username or password cannot be null or empty.");
        }

        // put username to pre login params map.
        setUsernameToPreLoginParams();

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
                throw new LoginFailedException(createWbpLoginExceptionMessage(response));
            }

            // get cookie from response headers
            Map<String, String> header = response.getHeader();
            String cookie = header.get("set-cookie");
            if (cookie == null) {
                cookie = header.get("Set-Cookie");
            }

            // 如果cookie并不符合预期
            if (cookie.length() < WbpConstants.COOKIE_LENGTH_THRESHOLD) {
                logger.debug("response cookie: " + cookie);
                String reason = getReason(response.getBody());
                if (reason != null) {
                    throw new LoginFailedException("登陆失败，原因：" + reason);
                }
                throw new LoginFailedException("登陆失败，未知原因。响应数据为：" + response.getBody());
            }

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
        params.put("rsakt", "mod");
        params.put("checkpin", "1");
        params.put("_", String.valueOf(System.currentTimeMillis()));
        return params;
    }

    private void setUsernameToPreLoginParams() {
        if (!preLoginParams.containsKey("su")) {
            preLoginParams.put("su", getUsername());
        }
    }

    private String createWbpLoginExceptionMessage(WbpHttpResponse response) {
        return "[ login failed message ]" + "do request login url failed" +
                "\n[ response code ] " + response.getStatusCode() +
                "\n[ response headers ]" + response.getHeader() +
                "\n[ response body ]" + response.getBody();
    }

    /**
     * 生成登陆需要的参数，也没什么办法写好看一点了
     * 这方法也写的太丑了
     *
     * @param preLogin 预登陆结果
     * @return 登陆参数
     * @throws LoginFailedException lfe
     */
    private Map<String, String> createLoginParams(PreLogin preLogin) throws LoginFailedException {
        // 根据微博加密js中密码拼接的方法
        String pwd = preLogin.getServertime() + "\t" + preLogin.getNonce() + "\n" + getPassword();

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
        } catch (NoSuchAlgorithmException | NoSuchPaddingException |
                InvalidKeySpecException | InvalidKeyException |
                IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
            throw new LoginFailedException("login failed. encrypt password failed. message: " + e.getMessage());
        }
        params.put("sr", "1920*1080");
        params.put("su", Base64.getEncoder().encodeToString(getUsername().getBytes()));
        params.put("url", "https://weibo.com/ajaxlogin.php?framelogin=1&callback=parent.sinaSSOController.feedBackUrlCallBack");
        params.put("useticket", "1");
        params.put("vsnf", "1");
        return params;
    }


    /**
     * 从登陆的响应结果中解析，获取登陆失败中响应的原因
     *
     * @param responseHtml response
     * @return reason
     * @throws UnsupportedEncodingException uee
     */
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

    /**
     * 从登陆的响应结果中解析
     *
     * @param responseHtml responseHtml
     * @return location未知的内容
     */
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
