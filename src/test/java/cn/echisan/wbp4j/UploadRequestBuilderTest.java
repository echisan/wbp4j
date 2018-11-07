package cn.echisan.wbp4j;

import cn.echisan.wbp4j.exception.Wbp4jException;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class UploadRequestBuilderTest {

    @Test
    public void build() throws IOException, Wbp4jException {
        WbpUploadRequest request = new UploadRequestBuilder()
                .setAcount("1916152345@qq.com", "Dengzhexuan123")
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
        URL resource = CookieContext.class.getClassLoader().getResource("");
        System.out.println(resource.getPath());
    }

    @Test
    public void path() throws IOException {
        String path = "C:/Users/E73AN/Desktop/wbp4j/target/test-classes/mycache11541583158662.txt";
        int i = path.lastIndexOf("/");
        String substring = path.substring(0, i+1);
        System.out.println(substring);
    }
}