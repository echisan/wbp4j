package cn.echisan;

import cn.echisan.wbp4j.Entity.PreLogin;
import cn.echisan.wbp4j.utils.WbpRequest;
import cn.echisan.wbp4j.utils.WbpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by echisan on 2018/6/13
 */
public class WbpRequestTest {

    private static final Logger logger = Logger.getLogger(WbpRequestTest.class);

    @Test
    public void doGet() throws IOException {

        String url = "https://login.sina.com.cn/sso/prelogin.php?" +
                "entry=weibo&su=MTIzNDU2&rsakt=mod&checkpin=1&client=ssologin.js(v1.4.19)&_="+System.currentTimeMillis();

        WbpRequest wbpRequest = new WbpRequest();
        WbpResponse wbpResponse = wbpRequest.doGet(url);

        ObjectMapper objectMapper = new ObjectMapper();
        PreLogin preLogin = objectMapper.readValue(wbpResponse.getBody(), PreLogin.class);
        logger.info(preLogin.toString());

    }
}
