package cn.echisan.wbp4j;

import cn.echisan.wbp4j.Entity.ImageInfo;

public interface UploadResponse {

    enum ResultStatus {
        SUCCESS, FAILED
    }

    void setResult(ResultStatus rs);

    ResultStatus getResult();

    void setMessage(String message);

    String getMessage();

    ImageInfo getImageInfo();

    void setImageInfo(ImageInfo imageInfo);
}
