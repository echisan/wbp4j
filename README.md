# wbp4j 
微博图床Java API

[![](https://jitpack.io/v/echisan/wbp4j.svg)](https://jitpack.io/#echisan/wbp4j)

# 简介

这是个使用了微博图床接口的上传图片的api

## 安装 MAVEN

首先添加一下仓库

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

再添加一下依赖

```xml
<dependency>
    <groupId>com.github.echisan</groupId>
    <artifactId>wbp4j</artifactId>
    <version>0.0.1</version>
</dependency>
```

然后就ok啦

## 使用

```java
WbpUpload wbpUpload = new WbpUploadBuilder().setAccount("username","password").build();
// 参数传入文件地址
ImageInfo imageInfo = wbpUpload.upload("F:\\img.jpg");
// 或者传入文件对象
wbpUpload.upload(file);
// 又或者直接传入图片的base64
wbpUpload.uploadB64(b64str);
```

结果

```
ImageInfo{
  pid='7fa15162gy1fsayytifkuj203903bglr', 
  width=117, 
  height=119, 
  size=7708, 
  large='//wx4.sinaimg.cn/large/7fa15162gy1fsayytifkuj203903bglr.jpg', 
  middle='//wx4.sinaimg.cn/mw690/7fa15162gy1fsayytifkuj203903bglr.jpg', 
  small='//wx4.sinaimg.cn/thumbnail/7fa15162gy1fsayytifkuj203903bglr.jpg'
}
```
