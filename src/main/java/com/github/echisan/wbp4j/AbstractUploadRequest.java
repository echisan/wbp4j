package com.github.echisan.wbp4j;

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
 * 本类是一个上传图片接口的抽象类，已经被设计成线程安全的类
 * 允许多线程情况下访问本类
 * <p>
 * 因为upload(byte[] bytes),upload(File file)到最后都是依赖upload(String base64)
 * 所以只需实现upload(String base64)接口即可
 * <p>
 * 但是调用该接口需要登陆获取cookie，过期则需要重新登陆
 * 根据单一职责原则，会发现上传接口与登陆功能强耦合
 * 上传不应该直接依赖登陆，所以引入拦截器模式
 * <p>
 * 调用上传接口upload(byte[] bytes)等接口时，会先通过一系列预定义好的拦截器
 * 最后才会真正调用上传操作
 * <p>
 * 由于上传参数，接口路径有可能会发生变化，为了尽量符合开闭原则
 * 将真正上传的接口doUpload(UploadAttributes uploadAttributes)交由子类去实现
 * 因该可以应付后续可能的改变吧
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


    @Override
    public UploadResponse upload(File file) throws IOException, UploadFailedException {
        return upload(imageToBase64(file));
    }

    @Override
    public UploadResponse upload(byte[] bytes) throws IOException, UploadFailedException {
        return upload(Base64.getEncoder().encodeToString(bytes));
    }

    @Override
    public UploadResponse upload(String base64) throws IOException, UploadFailedException {

        UploadAttributes attributes = new UploadAttributes();
        attributes.setBase64(base64);
        attributes.setContext(new HashMap<>());

        UploadContextHolder.setUploadAttributes(attributes);

        // pre process
        for (UploadInterceptor interceptor : uploadInterceptors) {
            boolean processBefore = interceptor.processBefore(UploadContextHolder.getUploadAttributes());
            // 如果return false则说明有拦截器进行了拦截，终止交易
            if (!processBefore) {
                WbpUploadResponse response = new WbpUploadResponse();
                response.setResult(UploadResponse.ResultStatus.FAILED);
                Object o = attributes.getContext().get(WbpConstants.UA_ERROR_MESSAGE);
                response.setMessage(o != null ? (String) o : "拦截器:[ " + interceptor.getClass().getName() + " ]拦截了上传操作");
                return response;
            }
        }

        UploadResponse uploadResponse = doUpload(UploadContextHolder.getUploadAttributes());

        // post process
        for (UploadInterceptor interceptor : uploadInterceptors) {
            interceptor.processAfter(uploadResponse);
        }

        // reset contextHolder attributes
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
