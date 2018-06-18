package cn.echisan.wbp4j;

import cn.echisan.wbp4j.Entity.ImageInfo;
import cn.echisan.wbp4j.Entity.UploadResp;
import cn.echisan.wbp4j.Entity.upload.Pic_1;
import cn.echisan.wbp4j.exception.Wbp4jException;
import cn.echisan.wbp4j.utils.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Base64;
import java.util.Collections;
import java.util.List;


/**
 * Created by echisan on 2018/6/13
 */
public class WbpUpload {

    private static final Logger logger = LoggerFactory.getLogger(WbpUpload.class);

    private static final String uploadUrl = "http://picupload.service.weibo.com/interface/pic_upload.php?" +
            "ori=1&mime=image%2Fjpeg&data=base64&url=0&markpos=1&logo=&nick=0&marks=1&app=miniblog";

    public WbpUpload(String username, String password) {
        this(Collections.singletonList(new Account(username,password)));
    }

    public WbpUpload(List<Account> accounts){
        if (!CookieHolder.exist()){
            for (Account account : accounts){
                // 如果登陆成功
                if (WbpLogin.login(account.getUsername(), account.getPassword())) {
                    break;
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        AccountHolder.setAccounts(accounts);
    }

    /**
     * 根据文件路径上传图片
     *
     * @param filePath 文件路径
     * @return ImageInfo
     * @throws IOException
     */
    public ImageInfo upload(String filePath) throws IOException, Wbp4jException {

        File file = new File(filePath);
        if (file.exists()) {
            return uploadB64(imageToBase64(file));
        }
        throw new Wbp4jException("文件[" + filePath + "]不存在");
    }

    /**
     * @param file file
     * @return ImageInfo
     * @throws IOException IOException
     */
    public ImageInfo upload(File file) throws IOException {
        return uploadB64(imageToBase64(file));
    }

    /**
     * 根据文件base64上传图片
     *
     * @param base64Img 图片的b64
     * @return ImageInfo
     * @throws IOException IOException
     */
    public ImageInfo uploadB64(String base64Img) throws IOException, Wbp4jException {
        WbpRequest wbpRequest = new WbpRequest();
        WbpResponse wbpResponse = wbpRequest.doPost(uploadUrl, base64Img);

        String body = wbpResponse.getBody();
        String respJson = getResponseJson(body);

        UploadResp uploadResp = null;
        try {
            uploadResp = new ObjectMapper().readValue(respJson, UploadResp.class);
        } catch (IOException e) {
            e.printStackTrace();
            throw new Wbp4jException("上传失败,返回了个奇怪的东西,可能是网络连接失败,"+e.getMessage());
        }

        int ret = uploadResp.getData().getPics().getPic_1().getRet();

        // 如果ret=-1 可能是cookies过期了
        if (ret == -1) {
            logger.error("未登录或cookie已过期, 正重新登录");
            boolean login = WbpLogin.login();
            if (login){
                return uploadB64(base64Img);
            }
            return null;
        }

        // 如果上传失败
        if (ret != 1) {
            logger.error("图片上传失败,我也不知道什么原因, " + uploadResp.toString());
            return null;
        }

        logger.info("图片上传成功!");
        Pic_1 pic_1 = uploadResp.getData().getPics().getPic_1();
        // 如果成功就生成图片信息
        return new ImageInfo(pic_1.getPid(), pic_1.getWidth(), pic_1.getHeight(), pic_1.getSize());
    }

    private String imageToBase64(File imageFile) {
        String base64Image = "";
        try (FileInputStream imageInFile = new FileInputStream(imageFile)) {
            // Reading a Image file from file system
            byte imageData[] = new byte[(int) imageFile.length()];
            imageInFile.read(imageData);
            base64Image = Base64.getEncoder().encodeToString(imageData);
        } catch (FileNotFoundException e) {
            System.out.println("Image not found" + e);
        } catch (IOException ioe) {
            System.out.println("Exception while reading the Image " + ioe);
        }
        return base64Image;
    }

    private String getResponseJson(String body) {
        int i = body.indexOf("</script>");
        return body.substring(i + 9);
    }

}
