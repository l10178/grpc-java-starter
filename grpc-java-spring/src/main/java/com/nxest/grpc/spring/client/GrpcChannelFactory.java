package com.nxest.grpc.spring.client;

import java.util.List;

import io.grpc.Channel;
import io.grpc.ClientInterceptor;

public interface GrpcChannelFactory {

    String DEFAULT_CHANEL_NAME = "default";

//    Channel createChannel();

    Channel createChannel(String name);

    Channel createChannel(String name, List<ClientInterceptor> interceptors);
}
