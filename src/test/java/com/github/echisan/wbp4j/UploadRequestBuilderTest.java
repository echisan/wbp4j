package com.github.echisan.wbp4j;

import com.github.echisan.wbp4j.exception.UploadFailedException;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class UploadRequestBuilderTest {

    @Test
    public void buildDefault() throws IOException, UploadFailedException {
        UploadRequest uploadRequest = UploadRequestBuilder.buildDefault("", "");

        UploadResponse response = uploadRequest.upload(new File("F:\\pics\\QQ图片20190323010547.gif"));

        System.out.println(response);

    }

    @Test
    public void custom() {

        UploadRequestBuilder.Builder builder = UploadRequestBuilder.custom("", "");
    }
}
