package cn.echisan.wbp4j;

import cn.echisan.wbp4j.Entity.ImageInfo;
import cn.echisan.wbp4j.Entity.UploadResp;
import cn.echisan.wbp4j.Entity.upload.Pic_1;
import cn.echisan.wbp4j.exception.Wbp4jException;
import cn.echisan.wbp4j.utils.WbpRequest;
import cn.echisan.wbp4j.utils.WbpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Base64;
/**
 * Created by echisan on 2018/6/13
 */
public class WbpUpload {

    private static final Logger logger = Logger.getLogger(WbpUpload.class);

    private static final String uploadUrl = "http://picupload.service.weibo.com/interface/pic_upload.php?" +
            "ori=1&mime=image%2Fjpeg&data=base64&url=0&markpos=1&logo=&nick=0&marks=1&app=miniblog";
    private static int reloginFlag = 0;

    public WbpUpload() {
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
            throw new Wbp4jException("上传失败,返回了个奇怪的东西,可能是网络连接失败," + e.getMessage());
        }

        int ret = uploadResp.getData().getPics().getPic_1().getRet();

        // 如果ret=-1 可能是cookies过期了
        if (ret == -1) {
            logger.error("未登录或cookie已过期, 正重新登录");
            if (reloginFlag == 0){
                WbpLogin.reLogin();
                reloginFlag = 1;
                return uploadB64(base64Img);
            }
            throw new Wbp4jException("重新登陆失败，不再进行登陆了");
        }

        // 如果上传失败
        if (ret != 1) {
            logger.debug("图片上传失败," + uploadResp.toString());
            throw new Wbp4jException("图片上传失败,我也不知道什么原因");
        }

        logger.info("图片上传成功!");
        Pic_1 pic_1 = uploadResp.getData().getPics().getPic_1();
        // 如果成功就生成图片信息
        return new ImageInfo(pic_1.getPid(), pic_1.getWidth(), pic_1.getHeight(), pic_1.getSize());
    }

    public ImageInfo upload(byte[] bytes) throws IOException, Wbp4jException {
        String base64 = Base64.getEncoder().encodeToString(bytes);
        return uploadB64(base64);
    }

    public String imageToBase64(File imageFile) {
        String base64Image = "";
        try (FileInputStream imageInFile = new FileInputStream(imageFile)) {
            // Reading a Image file from file system
            byte imageData[] = new byte[(int) imageFile.length()];
            int read = imageInFile.read(imageData);
            logger.debug("read imageFile: [" + read + "]");
            base64Image = Base64.getEncoder().encodeToString(imageData);
        } catch (FileNotFoundException e) {
            logger.error("Image not found" + e);
        } catch (IOException ioe) {
            logger.error("Exception while reading the Image " + ioe);
        }
        return base64Image;
    }

    public static Builder builder() {
        return new Builder();
    }


    private String getResponseJson(String body) {
        int i = body.indexOf("</script>");
        return body.substring(i + 9);
    }

    public static class Builder {

        private String username = null;
        private String password = null;
        private String cookieFileName = null;
        private String cookiePath = null;
        private boolean enableCache = true;

        Builder() {
        }

        public Builder setCookiePath(String cookiePath) {
            this.cookiePath = cookiePath;
            return this;
        }

        public Builder setEnableCache(boolean enableCache) {
            this.enableCache = enableCache;
            return this;
        }

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setCookieFileName(String cookieFileName) {
            this.cookieFileName = cookieFileName;
            return this;
        }

        public WbpUpload build() throws IOException {

            if (enableCache) {
                if (cookieFileName != null) {
                    CookieHolder.setCookieFileName(cookieFileName);
                }
                if (cookiePath != null) {
                    CookieHolder.setCookiePath(cookiePath);
                }
            }
            if (username!=null && password!=null){
                WbpLogin.setUSERNAME(username);
                WbpLogin.setPASSWORD(password);
            } else {
                throw new IllegalArgumentException("username 或 password 不能为空!");
            }
            CookieHolder.setEnableCache(enableCache);
            if (!CookieHolder.hasCookieCache()){
                WbpLogin.login(username,password);
            }
            return new WbpUpload();
        }
    }
}
