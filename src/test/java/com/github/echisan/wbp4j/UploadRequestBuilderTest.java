package com.github.echisan.wbp4j;

import com.github.echisan.wbp4j.exception.Wbp4jException;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class UploadRequestBuilderTest {

    @Test
    public void build() throws IOException, Wbp4jException {
        WbpUploadRequest request = new UploadRequestBuilder()
                .setAcount("", "")
                .setCookieCacheName("mycache1" + System.currentTimeMillis())
                .setTryLoginTime(5 * 60 * 1000)
                .build();
        UploadResponse response = request.upload(new File("F:\\照片\\QQ图片20180227230831.jpg"));
        System.out.println(response.getResult());
        System.out.println(response.getMessage());
        System.out.println(response.getImageInfo());
    }
}