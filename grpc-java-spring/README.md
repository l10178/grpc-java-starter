# grpc-java-spring
 [![Build Status](https://travis-ci.org/l10178/grpc-java-starter.svg?branch=master)](https://travis-ci.org/l10178/grpc-java-starter)
 [![Maven Central]( https://maven-badges.herokuapp.com/maven-central/com.nxest.grpc/grpc-java-spring/badge.svg)]( https://maven-badges.herokuapp.com/maven-central/com.nxest.grpc/grpc-java-spring/)
 [![License](https://img.shields.io/github/license/mashape/apistatus.svg)](https://opensource.org/licenses/MIT)

Tools for automatically wiring up and starting a [gRPC][] service using Spring.

## Features
* Grpc Server: Configures and runs the embedded gRPC server with `@GrpcService` enabled beans as part of spring application.
* Grpc Client: Creates and manages your gRPC channels and stubs with `@GrpcClient`.
* Supports global and custom gRPC server/client interceptors.


## Usage
Add a dependency using Maven:
```xml
<dependency>
  <groupId>com.nxest.grpc</groupId>
  <artifactId>grpc-java-spring</artifactId>
  <version>0.0.3</version>
</dependency>
```

### gRPC server
See demo: [l10178/grpc-java-spring-test](https://github.com/l10178/grpc-java-starter/tree/master/grpc-java-spring-test)

### gRPC client
See demo: [l10178/grpc-java-spring-test](https://github.com/l10178/grpc-java-starter/tree/master/grpc-java-spring-test)

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
