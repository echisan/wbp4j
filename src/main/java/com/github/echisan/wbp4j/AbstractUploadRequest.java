package com.github.echisan.wbp4j;

import com.github.echisan.wbp4j.exception.LoginFailedException;
import com.github.echisan.wbp4j.exception.UploadFailedException;
import com.github.echisan.wbp4j.interceptor.UploadInterceptor;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

/**
 * 到底该不该用抽象呢
 * 本类扩展了UploadRequest接口
 * <p>
 * 应该把这个类设计成线程安全的类，有可能有多个线程同时请求该类
 */
public abstract class AbstractUploadRequest implements UploadRequest {
    private static final Logger logger = Logger.getLogger(AbstractUploadRequest.class);

    /**
     * 拦截器，用于执行上传前/后的操作
     * 如果有其中一个返回false则不会上传
     */
    private final List<UploadInterceptor> uploadInterceptors;

    public AbstractUploadRequest(List<UploadInterceptor> uploadInterceptors) {
        this.uploadInterceptors = uploadInterceptors;
    }

    public AbstractUploadRequest() {
        this(new ArrayList<>());
    }


    public UploadResponse upload(File file) throws IOException, UploadFailedException, LoginFailedException {
        return upload(imageToBase64(file));
    }

    public UploadResponse upload(byte[] bytes) {
        return upload(Base64.getEncoder().encode(bytes));
    }

    @Override
    public UploadResponse upload(String base64) throws IOException, UploadFailedException, LoginFailedException {

        UploadAttributes attributes = new UploadAttributes();
        attributes.setBase64(base64);
        attributes.setContext(new HashMap<>());

        UploadContextHolder.setUploadAttributes(attributes);

        // pre process
        for (UploadInterceptor interceptor : uploadInterceptors) {
            boolean processBefore = interceptor.processBefore(UploadContextHolder.getUploadAttributes());
            if (!processBefore){
                break;
            }
        }

        UploadResponse uploadResponse = doUpload(UploadContextHolder.getUploadAttributes());

        // post process
        for (UploadInterceptor interceptor : uploadInterceptors) {
            interceptor.processAfter(uploadResponse);
        }
        UploadContextHolder.resetAttributes();
        return uploadResponse;
    }

    protected abstract UploadResponse doUpload(UploadAttributes uploadAttributes) throws IOException, UploadFailedException;

    /**
     * 负责将file文件转成base64，主要为了适用于上传图片的接口
     *
     * @param imageFile the imageFile
     * @return image base64
     */
    private String imageToBase64(File imageFile) {
        String base64Image = "";
        try (FileInputStream imageInFile = new FileInputStream(imageFile)) {
            // Reading a Image file from file system
            byte[] imageData = new byte[(int) imageFile.length()];
            int read = imageInFile.read(imageData);
            logger.debug("read imageFile: [" + read + "]");
            base64Image = Base64.getEncoder().encodeToString(imageData);
        } catch (FileNotFoundException e) {
            logger.error("Image not found" + e);
        } catch (IOException ioe) {
            logger.error("Exception while reading the Image " + ioe);
        }
        return base64Image;
    }

}
