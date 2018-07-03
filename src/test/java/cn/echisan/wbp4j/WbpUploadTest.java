package cn.echisan.wbp4j;

import cn.echisan.wbp4j.Entity.ImageInfo;
import org.junit.Test;

import java.io.IOException;

public class WbpUploadTest {

    @Test
    public void upload() throws IOException {

        WbpUpload wbpUpload = WbpUpload.builder()
                .setUsername("")
                .setPassword("")
                .setCookiePath("F:\\桌面\\")
                .build();
        ImageInfo upload = wbpUpload.upload("F:\\桌面\\734111297235572746.jpg");
        System.out.println(upload);
    }
}