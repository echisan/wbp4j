package cn.echisan.wpb4j;

import cn.echisan.wpb4j.Entity.ImageInfo;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class WbpUploadTest {

    @Test
    public void upload() throws IOException {

        File file = new File("F:\\桌面\\QQ截图20180422010459.png");
        System.out.println(file.exists());
    }
}