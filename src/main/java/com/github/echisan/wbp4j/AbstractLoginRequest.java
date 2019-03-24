package com.github.echisan.wbp4j;

import java.util.HashMap;
import java.util.Map;

/**
 * 扩展了LoginRequest接口
 * 毕竟需要用户名跟密码
 * <p>
 * 但是不应该每个上层调用者都需要知道用户名密码
 * 所以LoginRequest中只存在了一个空参数的登陆方法login()
 * <p>
 * 账号密码只需要本类以及本类的子类知道可以了
 */
public abstract class AbstractLoginRequest implements LoginRequest {
    private String username;
    private String password;

    public void setUsernamePassword(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    /**
     * 检查一下调用者填写的账号，
     * 早点检查一下是否合法
     * 免得分明没填账号就在那里请求登陆，不浪费资源了
     *
     * @return 如果没问题就return true
     */
    public boolean checkAccount() {
        if (username == null || password == null) {
            return false;
        }
        return !username.trim().equals("") && !password.trim().equals("");
    }

    /**
     * 生成默认的登陆请求头
     *
     * @return 请求头
     */
    protected Map<String, String> getDefaultLoginHeader() {
        Map<String, String> header = new HashMap<>();
        header.put("Referer", "https://weibo.com/");
        header.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.79 Safari/537.36\"");
        header.put("Content-Type", "application/x-www-form-urlencoded");
        header.put("Accept", "text/html,application/xhtml+xm…plication/xml;q=0.9,*/*;q=0.8");
        header.put("Accept-Encoding", "deflate, br");
        header.put("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
        return header;
    }
}
