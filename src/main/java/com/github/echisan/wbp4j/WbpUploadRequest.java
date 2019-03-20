package com.github.echisan.wbp4j;

import com.github.echisan.wbp4j.exception.UploadFailedException;
import com.github.echisan.wbp4j.http.WbpHttpRequest;
import com.github.echisan.wbp4j.http.WbpHttpResponse;
import com.github.echisan.wbp4j.interceptor.UploadInterceptor;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;

import static java.net.HttpURLConnection.HTTP_OK;

public class WbpUploadRequest extends AbstractUploadRequest {
    private static final Logger logger = Logger.getLogger(WbpUploadRequest.class);

    /**
     * http请求工具类
     * 1、目前默认用的是HttpURLConnection实现
     * 2、其实之前也有用HttpClient实现过的，为了减少依赖去掉了
     * 如果有需要可以添加上
     */
    private final WbpHttpRequest wbpHttpRequest;

    public WbpUploadRequest(List<UploadInterceptor> uploadInterceptors, WbpHttpRequest wbpHttpRequest) {
        super(uploadInterceptors);
        this.wbpHttpRequest = wbpHttpRequest;
    }

    /**
     * 上传图片的请求头
     * 主要为请求接口提供必要的字段，比如 cookie
     */

    @Override
    protected UploadResponse doUpload(UploadAttributes uploadAttributes) throws IOException, UploadFailedException {

        WbpHttpResponse response = wbpHttpRequest.doPostMultiPart(uploadAttributes.getUrl(),
                uploadAttributes.getHeaders(),
                uploadAttributes.getBase64());
        if (response.getStatusCode() == HTTP_OK) {
            logger.debug(createWbpUploadDebugMessage("doUpload", response));
        } else {
            throw new UploadFailedException(createWbpUploadExceptionMessage("status code return not 200", response));
        }
        return new WbpUploadResponse();
    }


    private String createWbpUploadExceptionMessage(String message, WbpHttpResponse response) {
        return "[ upload failed message ]" + message +
                "\n[ response code ] " + response.getStatusCode() +
                "\n[ response headers ]" + response.getHeader() +
                "\n[ response body ]" + response.getBody();
    }

    private String createWbpUploadDebugMessage(String message, WbpHttpResponse response) {
        return "[ upload debug message ]" + message +
                "\n[ response code ] " + response.getStatusCode() +
                "\n[ response headers ]" + response.getHeader() +
                "\n[ response body ]" + response.getBody();
    }

}

