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


    /**
     * 提供一个包装方法，虽然入参是File，但是内部会把file转换成base64再调用上传接口上传
     *
     * @param file image file
     * @return uploadResponse
     * @throws IOException           ioe
     * @throws UploadFailedException ufe
     */
    @Override
    public UploadResponse upload(File file) throws IOException, UploadFailedException {
        return upload(imageToBase64(file));
    }

    /**
     * 提供一个byte数组的一个包装方法，可以方便的为springMVC中的multipartFile提供上传接口
     * spring中仅需upload(multipartFile.getBytes())即可
     *
     * @param bytes bytes数组
     * @return uploadResponse
     * @throws IOException           ioe
     * @throws UploadFailedException ufe
     */
    @Override
    public UploadResponse upload(byte[] bytes) throws IOException, UploadFailedException {
        return upload(Base64.getEncoder().encodeToString(bytes));
    }

    /**
     * 真正的上传接口，主要上传base64数据到微博接口
     *
     * @param base64 b64
     * @return uploadResponse
     * @throws IOException           ioe
     * @throws UploadFailedException ufe
     */
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

    /**
     * 真正的上传逻辑在这里，upload(String base64)负责调用本方法
     * 由于采用了拦截器模式，处理拦截器列表不应该直接与上传操作耦合
     * 所以应该交由子类去负责上传，子类只需实现本方法即可
     *
     * @param uploadAttributes uploadAttributes上传图片的参数
     * @return uploadResponse
     * @throws IOException           ioe
     * @throws UploadFailedException ufe
     */
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
