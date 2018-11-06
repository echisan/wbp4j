package cn.echisan.wbp4j;

import cn.echisan.wbp4j.exception.Wbp4jException;
import cn.echisan.wbp4j.http.DefaultWbpHttpRequest;
import cn.echisan.wbp4j.http.WbpHttpRequest;

import java.io.IOException;

/**
 * Created by echisan on 2018/11/5
 */
public class UploadRequestFactory {

    public static WbpUploadRequest createWbpHttpRequest(String username,String password) throws IOException, Wbp4jException {
        WbpHttpRequest httpRequest = new DefaultWbpHttpRequest();
        return new WbpUploadRequest(httpRequest,username,password);
    }
}
