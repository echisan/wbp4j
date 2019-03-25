package com.github.echisan.wbp4j;

import com.github.echisan.wbp4j.entity.ImageInfo;

/**
 * 响应结果接口
 * 虽然好像并没有什么必要写成接口
 */
public interface UploadResponse {

    enum ResultStatus {
        SUCCESS, FAILED, RETRY
    }

    void setResult(ResultStatus rs);

    ResultStatus getResult();

    void setMessage(String message);

    String getMessage();

    ImageInfo getImageInfo();

    void setImageInfo(ImageInfo imageInfo);
}
