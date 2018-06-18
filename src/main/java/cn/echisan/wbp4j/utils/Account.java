package cn.echisan.wbp4j.utils;

/**
 * Created by echisan on 2018/6/17
 */
public class Account {

    /**
     * 账号
     */
    private String username;
    /**
     * 密码
     */
    private String password;

    public Account(String username, String PASSWORD) {
        this.username = username;
        this.password = PASSWORD;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
