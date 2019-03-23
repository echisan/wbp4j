# wbp4j
微博图床Java API

# 简介
这是个使用了微博图床接口的上传图片的api
- 使用方便简单
- cookie过期自动登录

## Maven
添加依赖

```xml

```

## quickstart

```java

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


## Spring中使用

##  目前可能存在的问题
- 似乎部署到服务器上登陆会失败哦


## 更新日志
> 重构代码，代码结构更清晰稳定，减低各模块的耦合。修复缓存文件位置错误的问题，修复上传图片格式问题，支持了上传gif————2019.03.23

> 重构了代码，减少第三方依赖，目前只依赖logging，fastjson,将包上传至官方仓库使用更方便————2018.11.08

