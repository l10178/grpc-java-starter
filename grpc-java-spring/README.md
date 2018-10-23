# grpc-java-starter
grpc-java-starter

# 前期考虑
当我接触到这样一个全新的技术的时候，在考虑什么：
1. 能承担多少并发量,要不要做连接池，要不要做集群和负载均衡,用什么做。
2. 如何做认证和授权,如何管理session。
3. 如何给通信通道加密,比如S5/TLS或者支持自定义加密算法。
4. 如何与sping融合,方便大家使用。
5. 统一的切入点:日志、监控、调用统计等。
6. 错误处理:超时、未捕捉异常等错误。
7. 老版本兼容：增加或删除一个字段，增加或删除一个方法。
8. 有哪些已知的坑,提前规避。
9. 基本使用，同步/异步、单向双向

我想做成什么样：
1. 首先是根据项目需要来，先满足项目要求。先满足基本使用，并配合Spring使用，这是我们项目的基本要求。
2. 看下别人做了什么，别人需要什么，尽可能通用，满足更多人的需求。
3. GRPC有哪些常用配置。

从这个项目中能获取什么：
1. GRPC使用。
2. 已经踩过的坑记录下来，前车之鉴。


# Usage

## config
## Custom gRPC Server Configuration

哪些东西可配置，配置的入口和方式是什么，考虑以后做Spring boot版本如何快速Auto Configuration
port
ssl
Interceptor
logger：java.util.logging.Logger


errorhandling
manualflowcontrol

HeaderServerInterceptor
HeaderClientInterceptor
认证和授权(统一的接口，可选是否启用认证，以及可选认证方式，比如basic、jwt)

diff in-process and netty server

to many dependency

client sdk

thread safe

mock test

howto enable ProtoReflectionService


# Contributing

# Thanks

[salesforce/grpc-java-contrib](https://github.com/salesforce/grpc-java-contrib)
[yidongnan/grpc-spring-boot-starter](https://github.com/yidongnan/grpc-spring-boot-starter)
[LogNet/grpc-spring-boot-starter](https://github.com/LogNet/grpc-spring-boot-starter)
