package cn.echisan.wbp4j;

import cn.echisan.wbp4j.Entity.ImageInfo;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class WbpUploadTest {

    @Test
    public void upload() throws IOException {

        WbpUpload wbpUpload = new WbpUpload("","");
        ImageInfo upload = wbpUpload.upload("F:\\桌面\\734111297235572746.jpg");
        System.out.println(upload);
    }
}