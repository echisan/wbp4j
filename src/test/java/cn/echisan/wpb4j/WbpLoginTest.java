package cn.echisan.wpb4j;

import org.junit.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class WbpLoginTest {

    @Test
    public void login() throws NoSuchPaddingException,
            NoSuchAlgorithmException, IOException, BadPaddingException,
            IllegalBlockSizeException, InvalidKeyException, InvalidKeySpecException {

        WbpLogin loginWeibo = new WbpLogin();
        loginWeibo.login("","");
    }
}