package com.github.echisan.wbp4j;

/**
 * 为每条线程绑定一个上传参数对象
 * com.github.echisan.wbp4j.UploadAttributes在多线程环境下是不安全的
 * 在多线程环境下，每条线程调用com.github.echisan.wbp4j.AbstractUploadRequest中的
 * doUpload(UploadAttributes uploadAttributes)方法，每个拦截器都会更改当中的内容
 * <p>
 * 换句话说就是，多条线程中的多个拦截器去操作同一个UploadAttributes的后果是不可想象的
 * 所以，通过本类，为每个线程绑定一个UploadAttributes，每条线程互不干扰
 * 所以每条线程中的一整条拦截器链处理的是自己的UploadAttributes
 * <p>
 * 抄一下spring中的RequestContextHolder
 * 我也用抽象类
 */
public abstract class UploadContextHolder {

    /**
     * 绑定每条线程的参数
     */
    private static final ThreadLocal<UploadAttributes> uploadAttributesHolder = new ThreadLocal<>();


    /**
     * 获取当前线程的UploadAttributes对象
     *
     * @return 当前线程的UploadAttributes
     */
    public static UploadAttributes getUploadAttributes() {
        UploadAttributes uploadAttributes = uploadAttributesHolder.get();

        if (uploadAttributes == null) {
            throw new NullPointerException("upload context holder cannot find current uploadRequest..");
        }
        return uploadAttributes;
    }


    /**
     * 为当前线程绑定一个UploadAttributes
     *
     * @param uploadAttributes UploadAttributes
     */
    public static void setUploadAttributes(UploadAttributes uploadAttributes) {
        if (uploadAttributes == null) {
            throw new IllegalArgumentException("uploadAttributes cannot be null!");
        }
        uploadAttributesHolder.set(uploadAttributes);
    }

    /**
     * 清除当前线程的UploadAttributes对象
     * <p>
     * 当每一个请求或线程结束了操作
     * 应该把当前线程的UploadAttributes对象移除，避免内存泄漏
     */
    public static void resetAttributes() {
        uploadAttributesHolder.remove();
    }

}

