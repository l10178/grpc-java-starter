package com.nxest.grpc.spring.server;

import io.grpc.ServerBuilder;

/**
 * 留一个入口，用于自定义ServerBuilder的其他信息。
 */
public class GrpcServerBuilderConfigurer {

    public void configure(ServerBuilder<?> serverBuilder) {

    }
}
