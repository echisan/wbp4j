package com.github.echisan.wbp4j;

import com.github.echisan.wbp4j.exception.UploadFailedException;

import java.io.IOException;

/**
 * 可重试的上传模板
 * <p>
 * 请保证本类的子类应该是线程安全的
 */
public abstract class RetryableUploadRequest extends AbstractUploadRequest {

    private AbstractUploadRequest uploadRequest;

    public RetryableUploadRequest(AbstractUploadRequest uploadRequest) {
        this.uploadRequest = uploadRequest;
    }

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
     *
     * @param uploadResponse uploadResponse
     * @return 如果返回true则会进行重试，如果未false则直接返回结果
     */
    public abstract boolean shouldRetry(UploadResponse uploadResponse);
}
