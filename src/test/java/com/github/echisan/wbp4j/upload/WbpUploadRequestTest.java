package com.github.echisan.wbp4j.upload;

import com.github.echisan.wbp4j.UploadRequest;
import com.github.echisan.wbp4j.UploadRequestBuilder;
import com.github.echisan.wbp4j.UploadResponse;
import com.github.echisan.wbp4j.WbpUploadRequest;
import com.github.echisan.wbp4j.exception.LoginFailedException;
import com.github.echisan.wbp4j.exception.UploadFailedException;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class WbpUploadRequestTest {

    @Test
    public void upload() throws LoginFailedException, IOException, UploadFailedException {

        UploadRequest uploadRequest = UploadRequestBuilder.buildDefault("", "");

        UploadResponse uploadResponse = ((WbpUploadRequest) uploadRequest).upload(new File("/Users/echisan/Downloads/giftest.gif"));
        System.out.println(uploadResponse);

    }
}