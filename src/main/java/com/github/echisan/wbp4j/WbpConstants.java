package com.github.echisan.wbp4j;

/**
 * 一些常量
 */
public class WbpConstants {

    /**
     * 标注需要登陆
     */
    public static final String REQUIRE_LOGIN = "require_login";

    /**
     * 标注需要重新登陆
     */
    public static final String REQUIRE_RETRY_LOGIN = "require_retry_login";

    /**
     * UploadAttribute.getContext()中用于保存错误信息的key
     */
    public static final String UA_ERROR_MESSAGE = "upload_attributes_error_message";

    /**
     * cookie长度阈值，如果长度小于50基本上可以断定该cookie是无效的
     */
    public static final int COOKIE_LENGTH_THRESHOLD = 50;
}
