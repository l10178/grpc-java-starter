package com.nxest.grpc.spring.test.server.config;

import com.nxest.grpc.spring.server.GrpcServerRunner;
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
//        try {
//            runner.start();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return runner;
    }
}
