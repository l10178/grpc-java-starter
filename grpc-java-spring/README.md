# grpc-java-starter
Tools for automatically wiring up and starting a [gRPC][] service using Spring.

## Features
* Grpc Server: Configures and runs the embedded gRPC server with `@GrpcService` enabled beans as part of spring application. 
* Grpc Client: Automatically creates and manages your grpc channels and stubs with `@GrpcClient`.
* Supports global and custom gRPC server/client interceptors.


## Usage

### gRPC server


# 前期考虑
当我接触到这样一个全新的技术的时候，在考虑什么：
1. 能承担多少并发量,要不要做连接池，要不要做集群和负载均衡,用什么做。
2. 如何做认证和授权,如何管理session。
3. 如何给通信通道加密。
4. 如何与sping融合,方便大家使用。
5. 统一的切入点:日志、监控、调用统计等。
6. 错误处理:超时、未捕捉异常等错误。
7. 老版本兼容：增加或删除一个字段，增加或删除一个方法。
8. 有哪些已知的坑,提前规避。
9. 基本使用，同步/异步、单向双向


主要介绍
SSL配置：三种文件差别，生成一个证书
thread pool
interceptors order


TODO:
1. 

超时处理


#### Roadmap

errorhandling
manualflowcontrol
认证和授权(统一的接口，可选是否启用认证，以及可选认证方式，比如basic、jwt)

thread safe

mock test

how to enable ProtoReflectionService

diff in-process and netty server InProcessServerBuilder VS NettyServerBuilder


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


## Troubleshooting


## Thanks
* [salesforce/grpc-java-contrib](https://github.com/salesforce/grpc-java-contrib)
* [yidongnan/grpc-spring-boot-starter](https://github.com/yidongnan/grpc-spring-boot-starter)
* [LogNet/grpc-spring-boot-starter](https://github.com/LogNet/grpc-spring-boot-starter)


## License
Licensed under [MIT][]. Copyright (c) 2018 [l10178][]

[MIT]: https://opensource.org/licenses/MIT
[l10178]: http://nxest.com/
[gRPC]: https://grpc.io/
