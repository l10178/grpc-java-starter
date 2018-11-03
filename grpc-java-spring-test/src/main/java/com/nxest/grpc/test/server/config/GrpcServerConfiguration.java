package com.nxest.grpc.test.server.config;

import com.nxest.grpc.server.*;
import com.nxest.grpc.test.config.GrpcProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class GrpcServerConfiguration {

    @Resource
    private GrpcProperties grpcProperties;

    @Bean
    public GrpcServiceDiscoverer serviceDiscoverer() {
        return new AnnotationGrpcServiceDiscoverer();
    }

    @Bean
    public GrpcServerFactory severFactory() {
        return new NettyGrpcServerFactory(serviceDiscoverer(), grpcProperties.getServer());
    }

    @Bean(name = "grpcServer", initMethod = "start", destroyMethod = "destroy")
    public GrpcServer serverRunner() {
        return severFactory().createServer();
    }
}
