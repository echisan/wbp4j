package com.github.echisan.wbp4j.entity;

/**
 * Created by echisan on 2018/6/14
 */
public class ImageInfo {

    // 照片id
    private String pid;
    // 宽度
    private Integer width;
    // 长度
    private Integer height;
    // 大小
    private Integer size;
    // 原图url
    private String large;
    // 中等尺寸
    private String middle;
    // 缩略图
    private String small;

    public ImageInfo() {
    }

    public ImageInfo(String pid,
                     Integer width,
                     Integer height,
                     Integer size,
                     String large,
                     String middle,
                     String small) {
        this.pid = pid;
        this.width = width;
        this.height = height;
        this.size = size;
        this.large = large;
        this.middle = middle;
        this.small = small;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getLarge() {
        return large;
    }

    public void setLarge(String large) {
        this.large = large;
    }

    public String getMiddle() {
        return middle;
    }

    public void setMiddle(String middle) {
        this.middle = middle;
    }

    public String getSmall() {
        return small;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    @Override
    public String toString() {
        return "ImageInfo{" +
                "pid='" + pid + '\'' +
                ", width=" + width +
                ", height=" + height +
                ", size=" + size +
                ", large='" + large + '\'' +
                ", middle='" + middle + '\'' +
                ", small='" + small + '\'' +
                '}';
    }
}
