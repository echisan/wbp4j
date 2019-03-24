package com.github.echisan.wbp4j;

import com.github.echisan.wbp4j.exception.UploadFailedException;
import com.github.echisan.wbp4j.interceptor.UploadInterceptor;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class UploadRequestBuilderTest {

    @Test
    public void buildDefault() throws IOException, UploadFailedException {
        UploadRequest uploadRequest = UploadRequestBuilder.buildDefault("", "");
        UploadResponse response = uploadRequest.upload(new File(""));
        System.out.println(response);

    }

    @Test
    public void custom() throws IOException, UploadFailedException {

        UploadRequest uploadRequest = UploadRequestBuilder.custom("", "")
                .setCacheFilename("myCache")
                .addInterceptor(new UploadInterceptor() {
                    @Override
                    public boolean processBefore(UploadAttributes uploadAttributes) {
                        System.out.println("hello world");
                        return true;
                    }

                    @Override
                    public void processAfter(UploadResponse uploadResponse) {

                    }
                }).build();

        UploadResponse uploadResponse = uploadRequest.upload(new File(""));
        System.out.println(uploadResponse);
    }
}
