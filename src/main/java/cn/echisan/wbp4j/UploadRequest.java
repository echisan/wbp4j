package cn.echisan.wbp4j;

import cn.echisan.wbp4j.exception.Wbp4jException;

import java.io.File;
import java.io.IOException;

public interface UploadRequest {

    UploadResponse upload(File image) throws IOException, Wbp4jException;
}
