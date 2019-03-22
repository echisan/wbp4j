package com.github.echisan.wbp4j;

import com.github.echisan.wbp4j.exception.LoginFailedException;

/**
 * 登陆的接口
 * 因为似乎发现了还有另外一种登陆方式
 * 所以将方法抽离出来了
 * <p>
 * 在定义两个接口
 * 用于登陆成功或失败的响应结果
 */
public interface LoginRequest {

    void login() throws LoginFailedException;
}
