package com.nxest.grpc.spring.test.server.config;

import com.nxest.grpc.spring.server.GrpcServerRunner;
import com.nxest.grpc.spring.test.config.GrpcProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class GrpcServerConfiguration {

    @Resource
    private GrpcProperties grpcProperties;

    @Bean(name = "grpcServerRunner", initMethod = "start", destroyMethod = "destroy")
    public GrpcServerRunner serverRunner() {
        return new GrpcServerRunner(grpcProperties.getServer());
    }
}
