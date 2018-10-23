package com.nxest.grpc.spring.test.config;

import com.nxest.grpc.spring.GrpcServerRunner;
import com.nxest.grpc.spring.configure.GrpcServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class GrpcServerConfiguration {

    @Resource
    private GrpcProperties grpcProperties;

    @Bean(name = "grpcServerRunner", initMethod = "start", destroyMethod = "destroy")
    public GrpcServerRunner serverRunner() {
        GrpcServerRunner runner = new GrpcServerRunner();
        runner.setGrpcServerProperties(grpcProperties.getServer());
        return runner;
    }
}
