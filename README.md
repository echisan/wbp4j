# wbp4j （重构ing...）
微博图床Java API

# 重构接近完成(2018.11.06),文档日后再说好了

[![](https://jitpack.io/v/echisan/wbp4j.svg)](https://jitpack.io/#echisan/wbp4j)

# 简介
个人的一些思路

https://echisan.cn/article/15

这是个使用了微博图床接口的上传图片的api

- 支持cookie过期自动登录


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
    <version>0.0.3</version>
</dependency>
```

然后就ok啦

## 使用

```java
WbpUpload wbpUpload = WbpUpload.builder().setSinaAccount("username","pwd").build();
ImageInfo imageInfo = wbpUpload.upload("filename");
```

目前upload支持的参数 `文件路径`, `文件对象`, `图片的Base64(需调用wbpUpload.uploadB64())`,`bytes`

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
    return WbpUpload.builder().setSinaAccount("username","pwd").build();
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
    
    @RequestMapping("/wbp2")
    public String helloWbp(@RequestPart MultipartFile file) throws IOException {
        ImageInfo imageInfo = wbpUpload.upload(file.getBytes());
        return imageInfo.toString();
    }
}
```

