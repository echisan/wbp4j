package cn.echisan.wbp4j;

import cn.echisan.wbp4j.exception.Wbp4jException;
import cn.echisan.wbp4j.http.DefaultWbpHttpRequest;
import cn.echisan.wbp4j.http.WbpHttpRequest;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class UploadRequestFactoryTest {
    @Test
    public void test() throws IOException, Wbp4jException {
        WbpHttpRequest httpRequest = new DefaultWbpHttpRequest();
        UploadRequest request = new WbpUploadRequest(httpRequest, "", "");
        File file = new File("F:\\照片\\QQ图片20171106184014.gif");
        UploadResponse upload = request.upload(file);
        System.out.println(upload.getResult());
        System.out.println(upload.getMessage());
        System.out.println(upload.getImageInfo());
    }
}