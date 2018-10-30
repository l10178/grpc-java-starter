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

#### Roadmap

logger：java.util.logging.Logger
ssl
Interceptor: name and order, global or private
errorhandling
manualflowcontrol

HeaderServerInterceptor
HeaderClientInterceptor
认证和授权(统一的接口，可选是否启用认证，以及可选认证方式，比如basic、jwt)

diff in-process and netty server

thread safe

mock test route

how to enable ProtoReflectionService

InProcessServerBuilder VS NettyServerBuilder


客户端连接
1、全局配置
2、获取serverChannel
3、获取stub
4、发送
5、多server配置
6、如果server的IP、端口如果是动态的，如何。先满足单连接，静态IP端口


https://grpc.io/docs/guides/auth.html

gRPC 的 TLS 策略
gRPC 基于 HTTP/2 协议，默认会开启 SSL/TLS。gRPC 的 TLS 实现有两种策略：

1.  基于 OpenSSL 的 TLS
2.  基于 Jetty ALPN/NPN 的 TLS

对于非安卓的后端 Java 应用，gRPC 强烈推荐使用 OpenSSL，原因如下：

1.  性能更高：基于 OpenSSL 的 gRPC 调用比使用 JDK GCM 的性能高 10 倍以上；
2.  密码算法更丰富：OpenSSL 支持的密码算法比 JDK SSL 提供的更丰富，特别是 HTTP/2 协议使用的加密算法；
OpenSSL 支持 ALPN 回退到 NPN；
3.  不需要根据 JDK 的版本升级配套升级 ALPN 类库（Jetty 的 ALPN 版本与 JDK 特定版本配套使用）。
gRPC 的 HTTP/2 和 TLS 基于 Netty 框架实现，如果使用 OpenSSL，则需要依赖 Netty 的 netty-tcnative。


Netty 的 OpenSSL 有两种实现机制：Dynamic linked 和 Statically Linked。在开发和测试环境中，建议使用 Statically Linked 的方式（netty-tcnative-boringssl-static），它提供了对 ALPN 的支持以及 HTTP/2 需要的密码算法，不需要额外再集成 Jetty 的 ALPN 类库。从 1.1.33.Fork16 版本开始支持所有的操作系统，可以实现跨平台运行。

对于生产环境，则建议使用 Dynamic linked 的方式，原因如下：

很多场景下需要升级 OpenSSL 的版本或者打安全补丁，如果使用动态链接方式（例如 apt-ge），则应用软件不需要级联升级；
对于一些紧急的 OpenSSL 安全补丁，如果采用 Statically Linked 的方式，需要等待 Netty 社区提供新的静态编译补丁版本，可能会存在一定的滞后性。



Why not spring-boot-starter
1. 别人已经有了。
2. 我们的项目太老了，没有spring boot，还是xml配置文件。
3. 想用在Spring Boot项目中也并不难，尽力做到用起来简单，全部都有默认项。


//TODO:spring info 提示，bug:注入的chanel可能为null
 : Refreshing org.springframework.context.annotation.AnnotationConfigApplicationContext@1a72a540: startup date [Wed Oct 24 14:57:17 CST 2018]; root of context hierarchy
2018-10-24 14:57:17.782  INFO 31200 --- [           main] trationDelegate$BeanPostProcessorChecker : Bean 'grpcClientConfiguration' of type [com.nxest.grpc.spring.test.client.config.GrpcClientConfiguration$$EnhancerBySpringCGLIB$$8cb55dc8] is not eligible for getting processed by all BeanPostProcessors (for example: not eligible for auto-proxying)
2018-10-24 14:57:17.821  INFO 31200 --- [           main] trationDelegate$BeanPostProcessorChecker : Bean 'grpcChannelsProperties' of type [com.nxest.grpc.spring.client.configure.GrpcChannelsProperties] is not eligible for getting processed by all BeanPostProcessors (for example: not eligible for auto-proxying)
2018-10-24 14:57:17.825  INFO 31200 --- [           main] trationDelegate$BeanPostProcessorChecker : Bean 'grpcLoadBalancerFactory' of type [io.grpc.util.RoundRobinLoadBalancerFactory] is not eligible for getting processed by all BeanPostProcessors (for example: not eligible for auto-proxying)
2018-10-24 14:57:17.830  INFO 31200 --- [           main] trationDelegate$BeanPostProcessorChecker : Bean 'clientInterceptorRegistry' of type [com.nxest.grpc.spring.client.ClientInterceptorRegistry] is not eligible for getting processed by all BeanPostProcessors (for example: not eligible for auto-proxying)
2018-10-24 14:57:17.836  INFO 31200 --- [           main] trationDelegate$BeanPostProcessorChecker : Bean 'addressChannelFactory' of type [com.nxest.grpc.spring.client.AddressChannelFactory] is not eligible for getting processed by all BeanPostProcessors (for example: not eligible for auto-proxying)
2018-10-24 14:57:18.063  INFO 31200 --- [           main] c.n.grpc.spring.server.GrpcServerRunner  : Starting gRPC Server ...


# Contributing

# Thanks

[salesforce/grpc-java-contrib](https://github.com/salesforce/grpc-java-contrib)
[yidongnan/grpc-spring-boot-starter](https://github.com/yidongnan/grpc-spring-boot-starter)
[LogNet/grpc-spring-boot-starter](https://github.com/LogNet/grpc-spring-boot-starter)
