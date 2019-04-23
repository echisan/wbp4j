# wbp4j
> weibo picture api for java (中二一下)

使用Java实现的微博图床API，提供简单的api即可完成上传图片到微博图床，可方便集成到自己的项目当中。

如果有兴趣或奇怪的需求或者想看故事可以查看[说明文档](https://github.com/echisan/wbp4j/wiki)   
如果出现任何问题欢迎提issue、欢迎提pr   
如果这个项目帮助到你了欢迎star鼓励一下^^

# 特色
- 使用方便简单
- 获取简单，直接加入maven依赖即可
- cookie缓存
- cookie过期自动登录
- 第三方依赖少，仅依赖fastjson，logback
- 自由度高，一切均可自定义配置
  - 可自定义缓存介质
  - 可[自定义重试策略](https://github.com/echisan/wbp4j/wiki/%E8%87%AA%E5%AE%9A%E4%B9%89%E9%87%8D%E8%AF%95%E7%AD%96%E7%95%A5)
  - 可自定义[拦截器](https://github.com/echisan/wbp4j/wiki/%E6%8B%A6%E6%88%AA%E5%99%A8)添加自己的逻辑
- 仍在维护

## Maven
引入maven依赖即可
```xml
<dependency>
  <groupId>com.github.echisan</groupId>
  <artifactId>wbp4j</artifactId>
  <version>3.3</version>
</dependency>
```

## 用法

### 使用默认配置

```java
UploadRequest uploadRequest = UploadRequestBuilder.buildDefault("your username", "your password");
UploadResponse response = uploadRequest.upload(new File("go.png"));
```

### 自定义配置
支持自定义拦截器，具体查看文档

```java
UploadRequest uploadRequest = UploadRequestBuilder.custom("your username", "your password")
                .setCacheFilename("myCache")
                .addInterceptor(new UploadInterceptor() {
                    @Override
                    public boolean processBefore(UploadAttributes uploadAttributes) {
                        System.out.println("hello world");
                        return true;
                    }
                    @Override
                    public void processAfter(UploadResponse uploadResponse) {
                    }
                }).build();

UploadResponse uploadResponse = uploadRequest.upload(new File(""));
```

返回结果
```json
{
    "message": "上传图片成功",
    "imageInfo": {
        "pid": "7fa15162gy1g1e5o2vlmwj20dn07e0t7",
        "width": 491,
        "height": 266,
        "size": 27707,
        "large": "https://ws3.sinaimg.cn/large/7fa15162gy1g1e5o2vlmwj20dn07e0t7.jpg",
        "middle": "https://ws3.sinaimg.cn/mw690/7fa15162gy1g1e5o2vlmwj20dn07e0t7.jpg",
        "small": "https://ws3.sinaimg.cn/small/7fa15162gy1g1e5o2vlmwj20dn07e0t7.jpg"
    },
    "result": "SUCCESS"
}
```

## 使用

## Spring中使用

```java
@SpringBootApplication
public class DemoApplication {
    @Bean
    public UploadRequest uploadRequest() {
        return UploadRequestBuilder.buildDefault("your username", "your password");
    }
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}

@RestController
@RequestMapping("/wbp4j")
class TestController {

    @Autowired
    private UploadRequest uploadRequest;

    @PostMapping
    public WbpUploadResponse uploadImage(@RequestPart("file") MultipartFile multipartFile) throws IOException, UploadFailedException {
        UploadResponse upload = uploadRequest.upload(multipartFile.getBytes());
        // 推荐先做一个判断
        // if (response.getResult().equals(UploadResponse.ResultStatus.SUCCESS)) {
        //    做自己的响应封装
        //}
        return (WbpUploadResponse) upload;
    }

}
```

**注意：UploadRequest是一个线程安全的类，可直接注入到你想使用的类中去，不要每次调用上传api时都去调用`UploadRequestBuilder.build()`是没有任何意义的**


## 更新日志
> 修复了修改缓存文件名不生效的问题，增加了登陆失败返回的信息以及对unicode的解码 ———— 2019.04.23    
> 优化了重试代码，修复了重试机制还是不生效的问题 ———— 2019.03.30  
> 修复了重试机制不生效的问题 ———— 2019.03.25  
> 修复了部署到服务器后无法登陆的问题，修复了返回的图片格式问题 ————2019.03.24  
> 重构代码，代码结构更清晰稳定，减低各模块的耦合。修复缓存文件位置错误的问题，修复上传图片格式问题，支持了上传gif————2019.03.23  
> 重构了代码，减少第三方依赖，目前只依赖logging，fastjson,将包上传至官方仓库使用更方便————2018.11.08  

