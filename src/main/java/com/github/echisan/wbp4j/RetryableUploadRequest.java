package com.github.echisan.wbp4j;

import com.github.echisan.wbp4j.exception.UploadFailedException;

import java.io.IOException;

/**
 * 可重试的上传模板
 * <p>
 * 请保证本类的子类应该是线程安全的
 * 具体重试逻辑已经实现
 * <p>
 * 而重试的策略可能有多个，所以是否进行重试的判断交由子类去决定
 */
public abstract class RetryableUploadRequest extends AbstractUploadRequest {

    private AbstractUploadRequest uploadRequest;

    public RetryableUploadRequest(AbstractUploadRequest uploadRequest) {
        this.uploadRequest = uploadRequest;
    }

    public RetryableUploadRequest() {
    }

    /**
     * 应该是用while还是用if呢，纠结
     *
     * @param base64 b64
     * @return uploadResponse
     * @throws IOException           ioe
     * @throws UploadFailedException ufe
     */
    @Override
    public UploadResponse upload(String base64) throws IOException, UploadFailedException {
        UploadResponse response = uploadRequest.upload(base64);
        if (shouldRetry(response)) {
            return uploadRequest.upload(base64);
        }
        return response;
    }

    /**
     * 交由实现类去操作
     *
     * @param uploadAttributes uploadAttributes
     * @return UploadResponse
     * @throws IOException           IOException
     * @throws UploadFailedException UploadFailedException
     */
    @Override
    protected UploadResponse doUpload(UploadAttributes uploadAttributes) throws IOException, UploadFailedException {
        return uploadRequest.doUpload(uploadAttributes);
    }

    /**
     * 根据子类的重试策略判断是否重试
     * 可以有多种判断策略
     *
     * @param uploadResponse uploadResponse
     * @return 如果返回true则会进行重试，如果未false则直接返回结果
     */
    public abstract boolean shouldRetry(UploadResponse uploadResponse);

    public AbstractUploadRequest getUploadRequest() {
        return uploadRequest;
    }

    public void setUploadRequest(AbstractUploadRequest uploadRequest) {
        this.uploadRequest = uploadRequest;
    }
}
