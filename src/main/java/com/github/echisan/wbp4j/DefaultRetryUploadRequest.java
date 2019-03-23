package com.github.echisan.wbp4j;

/**
 * 默认的重试策略
 * <p>
 * 本策略只会在cookie过期导致的上传失败后进行重试
 * 而非登陆失败或者获取cookie失败而进行重试
 */
public class DefaultRetryUploadRequest extends RetryableUploadRequest {
    public DefaultRetryUploadRequest(AbstractUploadRequest uploadRequest) {
        super(uploadRequest);
    }

    /**
     * 只要返回的结果是RETRY则进行重试
     * 因为在com.github.echisan.wbp4j.WbpUploadRequest中定义了该响应结果
     *
     * @param uploadResponse uploadResponse
     * @return 是否需要重试
     */
    @Override
    public boolean shouldRetry(UploadResponse uploadResponse) {
        return uploadResponse.getResult().equals(UploadResponse.ResultStatus.RETRY);
    }
}
