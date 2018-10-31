package com.nxest.grpc.spring.test.client.config;

import com.nxest.grpc.spring.client.*;
import com.nxest.grpc.spring.client.configure.GrpcClientProperties;
import com.nxest.grpc.spring.test.config.GrpcProperties;
import com.nxest.grpc.spring.test.server.config.GrpcServerConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
//@EnableConfigurationProperties
//@ConditionalOnClass({GrpcChannelFactory.class})
@AutoConfigureAfter(GrpcServerConfiguration.class)
public class GrpcClientConfiguration {

    @Resource
    private GrpcProperties grpcProperties;

    @Bean
    public GrpcClientProperties grpcChannelProperties() {
        return grpcProperties.getClient();
    }

    @Bean
    public ClientInterceptorRegistry clientInterceptorRegistry() {
        return new ClientInterceptorRegistry();
    }

    @Bean
    public GrpcChannelFactory channelFactory() {
        return new AddressChannelFactory(grpcChannelProperties());
    }

    @Bean
    @ConditionalOnClass(GrpcClient.class)
    public GrpcClientBeanPostProcessor grpcClientBeanPostProcessor() {
        return new GrpcClientBeanPostProcessor(channelFactory(), clientInterceptorRegistry());
    }
}
