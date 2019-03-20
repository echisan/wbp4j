package com.github.echisan.wbp4j;

import com.github.echisan.wbp4j.UploadResponse;
import com.github.echisan.wbp4j.exception.LoginFailedException;
import com.github.echisan.wbp4j.exception.UploadFailedException;

import java.io.IOException;
import java.util.Map;

public interface UploadRequest {

    UploadResponse upload(String base64) throws IOException, UploadFailedException, LoginFailedException;

}
