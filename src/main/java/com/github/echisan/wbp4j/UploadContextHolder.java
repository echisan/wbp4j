package com.github.echisan.wbp4j;

/**
 * 为每条线程绑定一个上传参数对象
 */
public abstract class UploadContextHolder {

    /**
     * 绑定每条线程的参数
     */
    private static final ThreadLocal<UploadAttributes> uploadAttributesHolder = new ThreadLocal<>();


    public static UploadAttributes getUploadAttributes() {
        UploadAttributes uploadAttributes = uploadAttributesHolder.get();

        if (uploadAttributes == null) {
            throw new NullPointerException("upload context holder cannot find current uploadRequest..");
        }
        return uploadAttributes;
    }


    public static void setUploadAttributes(UploadAttributes uploadAttributes) {
        if (uploadAttributes == null) {
            throw new IllegalArgumentException("uploadAttributes cannot be null!");
        }
        uploadAttributesHolder.set(uploadAttributes);
    }

    public static void resetAttributes(){
        uploadAttributesHolder.remove();
    }

}

