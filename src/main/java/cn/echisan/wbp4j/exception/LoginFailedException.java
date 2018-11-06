package cn.echisan.wbp4j.exception;

/**
 * Created by echisan on 2018/11/5
 */
public class LoginFailedException extends Wbp4jException {

    public LoginFailedException() {
    }

    public LoginFailedException(String message) {
        super(message);
    }
}
