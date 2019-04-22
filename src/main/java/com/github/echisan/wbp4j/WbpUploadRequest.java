package com.github.echisan.wbp4j;

import com.alibaba.fastjson.JSON;
import com.github.echisan.wbp4j.entity.ImageInfo;
import com.github.echisan.wbp4j.entity.UploadResp;
import com.github.echisan.wbp4j.entity.upload.Pic_1;
import com.github.echisan.wbp4j.exception.UploadFailedException;
import com.github.echisan.wbp4j.http.DefaultWbpHttpRequest;
import com.github.echisan.wbp4j.http.WbpHttpRequest;
import com.github.echisan.wbp4j.http.WbpHttpResponse;
import com.github.echisan.wbp4j.interceptor.UploadInterceptor;
import com.github.echisan.wbp4j.utils.ImageSize;
import com.github.echisan.wbp4j.utils.WbpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

import static java.net.HttpURLConnection.HTTP_OK;

/**
 * 本来是UploadRequest的一个默认实现，也是线程安全的类
 */
public class WbpUploadRequest extends AbstractUploadRequest {
    private static final Logger logger = LoggerFactory.getLogger(WbpUploadRequest.class);

    /**
     * http请求工具类
     * 1、目前默认用的是HttpURLConnection实现
     * 2、其实之前也有用HttpClient实现过的，为了减少依赖去掉了
     * 如果有需要可以添加上
     */
    private final WbpHttpRequest wbpHttpRequest;

    public WbpUploadRequest(List<UploadInterceptor> uploadInterceptors) {
        this(uploadInterceptors, new DefaultWbpHttpRequest());
    }

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

        WbpUploadResponse uploadResponse = new WbpUploadResponse();
        uploadResponse.setResult(UploadResponse.ResultStatus.FAILED);

        WbpHttpResponse response = wbpHttpRequest.doPostMultiPart(uploadAttributes.getUrl(),
                uploadAttributes.getHeaders(),
                uploadAttributes.getBase64());
        if (response.getStatusCode() == HTTP_OK) {
            logger.debug(createWbpUploadDebugMessage("", response));

            UploadResp uploadResp = parseBodyJson(response.getBody());
            if (uploadResp == null) {
                uploadResponse.setMessage("无法解析返回结果，上传失败");
                return uploadResponse;
            }

            Pic_1 pic = uploadResp.getData().getPics().getPic_1();
            int ret = pic.getRet();

            if (ret == -1) {
                uploadResponse.setResult(UploadResponse.ResultStatus.RETRY);
                uploadResponse.setMessage("cookie已过期，请重新获取");
                logger.debug("cookie was expiration");
                return uploadResponse;
            }
            if (ret == -2) {
                uploadResponse.setResult(UploadResponse.ResultStatus.FAILED);
                uploadResponse.setMessage("上传的图片为空");
                return uploadResponse;
            }
            if (ret == -11) {
                uploadResponse.setResult(UploadResponse.ResultStatus.FAILED);
                uploadResponse.setMessage("上传的图片格式不正确");
                return uploadResponse;
            }
            if (ret != 1) {
                uploadResponse.setMessage("未知问题，retCode=" + ret + "可自行搜索该ret寻求结果");
                return uploadResponse;
            }

            uploadResponse.setResult(UploadResponse.ResultStatus.SUCCESS);
            uploadResponse.setMessage("上传图片成功");
            uploadResponse.setImageInfo(buildImageInfo(pic));
            return uploadResponse;

        } else {
            throw new UploadFailedException("状态码都不是200了，这谁都顶不住");
        }
    }

    private ImageInfo buildImageInfo(Pic_1 pic) {
        ImageInfo info = new ImageInfo();
        info.setPid(pic.getPid());
        info.setSize(pic.getSize());
        info.setWidth(pic.getWidth());
        info.setHeight(pic.getHeight());

        String pid = pic.getPid();
        info.setLarge(WbpUtils.getImageUrl(pid, ImageSize.large, true));
        info.setMiddle(WbpUtils.getImageUrl(pid, ImageSize.mw690, true));
        info.setSmall(WbpUtils.getImageUrl(pid, ImageSize.small, true));
        return info;
    }

    private UploadResp parseBodyJson(String body) {
        int i = body.indexOf("</script>");
        if (i == -1) {
            return null;
        }
        String substring = body.substring(i + 9);
        return JSON.parseObject(substring, UploadResp.class);
    }

    private String createWbpUploadDebugMessage(String message, WbpHttpResponse response) {
        return message +
                "\n[ response code ] " + response.getStatusCode() +
                "\n[ response headers ]" + response.getHeader() +
                "\n[ response body ]\n" + response.getBody();
    }

}

