package cn.echisan.wbp4j;

import cn.echisan.wbp4j.Entity.ImageInfo;
import org.junit.Test;

import java.io.IOException;

public class WbpUploadTest {

    @Test
    public void upload() throws IOException {

        WbpUpload wbpUpload = WbpUpload.builder().
                setSinaAccount("","")
                .build();
        ImageInfo upload = wbpUpload.upload("");
        System.out.println(upload);
    }
}