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
  <version>0.0.1</version>
</dependency>
```

### gRPC server

### gRPC client


## Troubleshooting
#### No name matching XXX found
* io.grpc.StatusRuntimeException: UNAVAILABLE: io exception
* Caused by: javax.net.ssl.SSLHandshakeException: General OpenSslEngine problem
* Caused by: java.security.cert.CertificateException: No name matching gRPC server name found
The name of the gRPC server name in the client config does not match the common / alternative name in the server certificate. You have to configure the grpc.client.(gRPC server name).security.authorityOverride property with a name that is present in the certificate.

## Thanks
* [salesforce/grpc-java-contrib](https://github.com/salesforce/grpc-java-contrib)
* [yidongnan/grpc-spring-boot-starter](https://github.com/yidongnan/grpc-spring-boot-starter)
* [LogNet/grpc-spring-boot-starter](https://github.com/LogNet/grpc-spring-boot-starter)


## License
Licensed under [MIT][]. Copyright (c) 2018 [l10178][]

[MIT]: https://opensource.org/licenses/MIT
[l10178]: http://nxest.com/
[gRPC]: https://grpc.io/
