package com.nxest.grpc.spring.test.client.config;

import com.nxest.grpc.spring.client.*;
import com.nxest.grpc.spring.client.configure.GrpcClientProperties;
import com.nxest.grpc.spring.test.server.config.GrpcServerConfiguration;
import io.grpc.LoadBalancer;
import io.grpc.util.RoundRobinLoadBalancerFactory;
import org.springframework.beans.factory.ListableBeanFactory;
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

    //    @ConditionalOnMissingBean
    @Bean
    public LoadBalancer.Factory grpcLoadBalancerFactory() {
        return RoundRobinLoadBalancerFactory.getInstance();
    }

    @Bean
    public GrpcChannelFactory addressChannelFactory() {
        return new AddressChannelFactory(grpcChannelProperties(), grpcLoadBalancerFactory());
    }

    @Bean
    @ConditionalOnClass(GrpcClient.class)
    public GrpcClientBeanPostProcessor grpcClientBeanPostProcessor(GrpcChannelFactory channelFactory, ClientInterceptorRegistry clientInterceptorRegistry) {
        return new GrpcClientBeanPostProcessor(channelFactory, clientInterceptorRegistry);
    }
}
