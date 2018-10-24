package com.nxest.grpc.spring.client.configure;

import java.util.Map;

import com.google.common.collect.Maps;

public class GrpcChannelsProperties {

    private final Map<String, GrpcChannelProperties> client = Maps.newHashMap();

    public GrpcChannelProperties getChannel(String name) {
        return client.getOrDefault(name, GrpcChannelProperties.DEFAULT);
    }

    public void addChannel(String name, GrpcChannelProperties channelProperties) {
        client.put(name, channelProperties);
    }

}
