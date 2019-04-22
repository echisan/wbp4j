package com.github.echisan.wbp4j;

import com.github.echisan.wbp4j.exception.UploadFailedException;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class UploadRequestBuilderTest {

    @Test
    public void buildDefault() throws IOException, UploadFailedException {
        UploadRequest uploadRequest = UploadRequestBuilder.buildDefault("1213123", "123213");
        UploadResponse response = uploadRequest.upload(new File("F:\\pics\\gopher.png"));
        System.out.println(response);

    }

    @Test
    public void custom() throws IOException, UploadFailedException {

        UploadRequest uploadRequest = UploadRequestBuilder.custom("", "")
                .setCacheFilename("F:\\Game\\wbpCache")
                .build();

        UploadResponse uploadResponse = uploadRequest.upload(new File(""));
        System.out.println(uploadResponse);
    }

}
