package cn.echisan.wbp4j;

import cn.echisan.wbp4j.exception.Wbp4jException;
import cn.echisan.wbp4j.http.DefaultWbpHttpRequest;
import cn.echisan.wbp4j.http.WbpHttpRequest;
import cn.echisan.wbp4j.http.WbpHttpResponse;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class UploadRequestBuilderTest {

    @Test
    public void build() throws IOException, Wbp4jException {
        WbpUploadRequest request = new UploadRequestBuilder()
                .setAcount("", "")
                .setCookieCacheName("mycache1" + System.currentTimeMillis())
                .setTryLoginTime(5 * 60 * 1000)
                .build();
        UploadResponse response = request.upload(new File("F:\\照片\\QQ图片20180227230831.jpg"));
        System.out.println(response.getResult());
        System.out.println(response.getMessage());
        System.out.println(response.getImageInfo());
    }

    @Test
    public void read() throws IOException {
        WbpHttpRequest request = new DefaultWbpHttpRequest();
        WbpHttpResponse wbpHttpResponse = request.doGet("https://echisan.cn");
        System.out.println(wbpHttpResponse.getBody());
    }
}