package com.github.echisan.wbp4j.interceptor;

import com.github.echisan.wbp4j.UploadAttributes;
import com.github.echisan.wbp4j.UploadResponse;

/**
 * 上传图片接口的拦截器接口
 * <p>
 * 主要用于拦截上传图片的接口，对参数进行相应的处理
 * 而拦截处理后虽然已经不能影响上传结果了
 * 但是可以根据UploadResponse的结果做一些操作
 */
public interface UploadInterceptor {

    /**
     * 在真正调用上传图片接口之前，会先调用此方法，可以对UploadAttributes中的参数进行操作
     * 可以放心修改该参数中的值，因为是线程安全的
     *
     * @param uploadAttributes 上传图片的参数
     * @return 如果返回true才会继续往下调用，返回false则中断，最后也不会调用上传图片的方法
     */
    boolean processBefore(UploadAttributes uploadAttributes);

    /**
     * 在调用上传接口完毕后，会调用本方法
     * 可以根据该相应结果进行相应的处理
     * 由于已经调用过上传接口了，再拦截也没有意义了，所以没有任何返回值
     *
     * @param uploadResponse 上传相应
     */
    void processAfter(UploadResponse uploadResponse);

}
