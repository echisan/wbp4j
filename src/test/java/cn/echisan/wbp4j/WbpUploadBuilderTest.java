package cn.echisan.wbp4j;

import cn.echisan.wbp4j.Entity.ImageInfo;
import org.junit.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class WbpUploadBuilderTest {


    @Test
    public void build() throws NoSuchPaddingException, NoSuchAlgorithmException,
            IOException, BadPaddingException, IllegalBlockSizeException,
            InvalidKeyException, InvalidKeySpecException {

        WbpLogin.login("","");
        ImageInfo imageInfo = new WbpUpload().upload("");
        System.out.println(imageInfo);
    }
}