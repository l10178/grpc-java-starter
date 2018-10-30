package com.nxest.grpc.spring.test.client.config;

import com.nxest.grpc.spring.client.*;
import com.nxest.grpc.spring.client.configure.GrpcClientProperties;
import com.nxest.grpc.spring.test.server.config.GrpcServerConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//@EnableConfigurationProperties
//@ConditionalOnClass({GrpcChannelFactory.class})
@AutoConfigureAfter(GrpcServerConfiguration.class)
public class GrpcClientConfiguration {

    //    @ConditionalOnMissingBean
    @Bean
    public GrpcClientProperties grpcChannelProperties() {
        GrpcClientProperties grpcChannelProperties = new GrpcClientProperties();
//        grpcChannelProperties.setNegotiationType("TLS");
        return grpcChannelProperties;
    }

    @Bean
    public ClientInterceptorRegistry globalClientInterceptorRegistry() {
        return new ClientInterceptorRegistry();
    }

    @Bean
    public GrpcChannelFactory addressChannelFactory() {
        return new AddressChannelFactory(grpcChannelProperties());
    }

    @Bean
    @ConditionalOnClass(GrpcClient.class)
    public GrpcClientBeanPostProcessor grpcClientBeanPostProcessor(GrpcChannelFactory channelFactory, ClientInterceptorRegistry clientInterceptorRegistry) {
        return new GrpcClientBeanPostProcessor(channelFactory, clientInterceptorRegistry);
    }
}
