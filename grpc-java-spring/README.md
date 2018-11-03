# grpc-java-spring
 [![Build Status](https://travis-ci.org/l10178/grpc-java-starter.svg?branch=master)](https://travis-ci.org/l10178/grpc-java-starter)
 [![Maven Central]( https://maven-badges.herokuapp.com/maven-central/com.nxest.grpc/grpc-java-spring/badge.svg)]( https://maven-badges.herokuapp.com/maven-central/com.nxest.grpc/grpc-java-spring/)
 [![License](https://img.shields.io/github/license/mashape/apistatus.svg)](https://opensource.org/licenses/MIT)
 
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
1. 超时处理
errorhandling
manualflowcontrol


认证和授权(统一的接口，可选是否启用认证，以及可选认证方式，比如basic、jwt)

thread safe

mock test

how to enable ProtoReflectionService

diff in-process and netty server InProcessServerBuilder VS NettyServerBuilder


https://grpc.io/docs/guides/auth.html


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
