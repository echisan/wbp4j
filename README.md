# wbp4j 
微博图床Java API

[![](https://jitpack.io/v/echisan/wbp4j.svg)](https://jitpack.io/#echisan/wbp4j)

# 简介

这是个使用了微博图床接口的上传图片的api

## Maven

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
    <version>0.0.2</version>
</dependency>
```

然后就ok啦

## 使用

### 直接使用

```java
WbpLogin.login("username", "password");
ImageInfo imageInfo = new WbpUpload().upload("F:\\example.jpg");
```

### 使用builder

```java
WbpUpload wbpUpload = new WbpUploadBuilder().setDev(true).setAccount("username","password").build();
ImageInfo imageInfo = wbpUpload.upload("filename");
```

目前upload支持的参数 `文件路径`, `文件对象`, `图片的Base64(需调用wbpUpload.uploadB64())`

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

## Spring中使用

```java
@Bean
public WbpUpload wbpUpload(){
    return new WbpUploadBuilder().setDev(true)
            .setAccount("username","password")
            .build();
}
```

```java
@RestController
public class HelloWbpController {

    @Autowired
    private WbpUpload wbpUpload;

    @RequestMapping("/wbp")
    public String helloWbp() throws IOException {
        ImageInfo imageInfo = wbpUpload.upload("F:\\example.jpg");
        return imageInfo.toString();
    }
}
```

