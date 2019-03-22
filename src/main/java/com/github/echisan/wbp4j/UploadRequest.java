package com.github.echisan.wbp4j;

import com.github.echisan.wbp4j.exception.UploadFailedException;

import java.io.File;
import java.io.IOException;

/**
 * 上传图片的接口
 * <p>
 * 提供三种上传方式
 * 虽然到最后都是调用upload(String base64)去实现的了。。
 */
public interface UploadRequest {

    UploadResponse upload(String base64) throws IOException, UploadFailedException;

    UploadResponse upload(byte[] bytes) throws IOException, UploadFailedException;

    UploadResponse upload(File file) throws IOException, UploadFailedException;

}
