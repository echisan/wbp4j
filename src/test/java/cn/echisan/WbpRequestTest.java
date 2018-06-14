package cn.echisan;

import cn.echisan.wpb4j.Entity.PreLogin;
import cn.echisan.wpb4j.utils.WbpRequest;
import cn.echisan.wpb4j.utils.WbpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by echisan on 2018/6/13
 */
public class WbpRequestTest {

    private static final Logger logger = LoggerFactory.getLogger(WbpRequestTest.class);

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
