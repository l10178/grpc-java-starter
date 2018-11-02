package com.nxest.grpc.spring.test.config;

import com.nxest.grpc.spring.client.configure.GrpcClientProperties;
import com.nxest.grpc.spring.server.configure.GrpcServerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("grpc")
public class GrpcProperties {

    /**
     * Grpc server properties
     */
    private final GrpcServerProperties server = new GrpcServerProperties();
    /**
     * Grpc client properties
     */
    private final GrpcClientProperties client = new GrpcClientProperties();

    public GrpcServerProperties getServer() {
        return server;
    }

    public GrpcClientProperties getClient() {
        return client;
    }
}
