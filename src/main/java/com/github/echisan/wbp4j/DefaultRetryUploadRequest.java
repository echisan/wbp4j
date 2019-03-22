package com.github.echisan.wbp4j;

/**
 * 默认的重试策略
 */
public class DefaultRetryUploadRequest extends RetryableUploadRequest {
    public DefaultRetryUploadRequest(AbstractUploadRequest uploadRequest) {
        super(uploadRequest);
    }

    @Override
    public boolean shouldRetry(UploadResponse uploadResponse) {
        return uploadResponse.getResult().equals(UploadResponse.ResultStatus.RETRY);
    }
}
