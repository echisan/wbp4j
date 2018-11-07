package com.github.echisan.wbp4j;

import com.github.echisan.wbp4j.Entity.ImageInfo;

import java.util.Random;

/**
 * Created by echisan on 2018/11/5
 */
public class ImageInfoBuilder {
    private ImageInfo imageInfo;
    private String extension = null;
    private static final Random random = new Random();
    private final String picUrl = "//ws%d.sinaimg.cn/%s/%s";

    public ImageInfoBuilder() {
        imageInfo = new ImageInfo();
    }

    public ImageInfoBuilder setImageInfo(String picId, int width, int height, int size) {
        imageInfo.setPid(picId);
        imageInfo.setWidth(width);
        imageInfo.setHeight(height);
        imageInfo.setSize(size);
        return this;
    }

    public ImageInfoBuilder setExtension(String extension) {
        this.extension = extension;
        return this;
    }

    public ImageInfo build() {
        int i = random.nextInt(3) + 1;
        this.imageInfo.setLarge(getPicUrl(i, "large"));
        this.imageInfo.setMiddle(getPicUrl(i, "mw690"));
        this.imageInfo.setSmall(getPicUrl(i, "thumbnail"));
        return this.imageInfo;
    }

    public ImageInfo buildWithoutUrl() {
        return this.imageInfo;
    }

    private String getPicUrl(int i, String size) {
        String ex = extension == null ? "jpg" : extension;
        return String.format(picUrl, i, size, this.imageInfo.getPid() + "." + ex);
    }

}
