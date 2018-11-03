package com.nxest.grpc.spring.client;

import io.grpc.Channel;
import io.grpc.ClientInterceptor;

import java.util.Collection;

public interface GrpcChannelFactory {

    String DEFAULT_CHANEL_NAME = "default";

    Channel createChannel();

    Channel createChannel(String name);

    Channel createChannel(String name, Collection<ClientInterceptor> interceptors);
}
