package com.github.echisan.wbp4j;

import com.github.echisan.wbp4j.exception.LoginFailedException;

/**
 * 登陆的接口
 * 因为似乎发现了还有另外一种登陆方式
 * 所以将方法抽离出来了
 */
public interface LoginRequest {

    /**
     * 空参方法，上层调用者只管调用
     * 假如接口设计成login(String username,String password)的话
     * 每个调用者都需要保存，或者通过某种方法获取到用户名密码
     * 这样就与登陆功能产生耦合了
     * 最好的方法我觉得应该就是保存在本接口的子类或者实现类中
     * <p>
     * 因为不允许运行时动态加载用户名密码或密码，所以肯定是线程安全的
     * 不过可以考虑添加该功能，有需要的话在说好了
     *
     * @throws LoginFailedException lfe
     */
    void login() throws LoginFailedException;
}
