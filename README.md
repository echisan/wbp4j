# wbp4j
微博图床Java API

# 简介
这是个使用了微博图床接口的上传图片的api
- 使用方便简单
- cookie过期自动登录

# 重构进度70%

# 目前可能存在的问题
- 似乎部署到服务器上登陆会失败哦
- 响应信息乱码问题可以自行修改request的请求头解决(大概)没得时间维护了
- 缓存的cookie文件位置也有点问题
- bug太多了 跑路了(

## Maven
添加依赖

```xml
<dependency>
  <groupId>com.github.echisan</groupId>
  <artifactId>wpb4j</artifactId>
  <version>2.0</version>
</dependency>
```

## quickstart

```java
UploadRequest uploadRequest = new UploadRequestBuilder()
                .setAcount("weibo username/email", "password")
                .build();
        UploadResponse response = uploadRequest.upload(new File("path"));
        System.out.println(response.getResult());
        System.out.println(response.getMessage());
        System.out.println(response.getImageInfo());
```

返回结果
```
SUCCESS
upload success!
ImageInfo{
    pid='7fa15162gy1fx0rsux7bcj20dn07e0t7',
    width=491, height=266, size=27707, 
    large='//ws3.sinaimg.cn/large/7fa15162gy1fx0rsux7bcj20dn07e0t7.jpg', 
    middle='//ws3.sinaimg.cn/mw690/7fa15162gy1fx0rsux7bcj20dn07e0t7.jpg', 
    small='//ws3.sinaimg.cn/thumbnail/7fa15162gy1fx0rsux7bcj20dn07e0t7.jpg'}
```

## 使用

目前可以设置的参数，除了`setAcount`是必须设置之外，其他都可以不配置
```java
UploadRequest uploadRequest = new UploadRequestBuilder()
                .setAcount("", "")
                .setTryLoginTime(重新登陆的时间间隔，默认2分钟，会根据重登的次数*间隔网上涨，直到登陆成功)
                .setCookieCacheName(登陆成功会对cookie缓存到本地，可以自定义缓存的名称)
                .setCookieCacheFilePath(自定义缓存的路径)
                .build();
```

## Spring中使用

```java
    @Bean
    public UploadRequest wbpUploadRequest(){
        return new UploadRequestBuilder()
                .setAcount("", "")
                .build();
    }
    
    @PostMapping("/wbp")
    public ImageInfo wbp(@RequestParam("image") MultipartFile file) throws IOException, Wbp4jException {

        // 需要将MultipartFile转化成file对象
        File f = new File(file.getName());
        FileOutputStream fos = new FileOutputStream(f);
        fos.write(file.getBytes());
        fos.close;
        UploadResponse upload = uploadRequest.upload(f);
        return upload.getImageInfo();
    }
```

## 更新日志
> 重构了代码，减少第三方依赖，目前只依赖logging，fastjson,将包上传至官方仓库使用更方便————2018.11.08

