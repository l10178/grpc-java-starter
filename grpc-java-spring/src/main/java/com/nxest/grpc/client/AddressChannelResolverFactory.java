package com.nxest.grpc.client;

import java.net.URI;

import javax.annotation.Nullable;

import com.nxest.grpc.client.configure.GrpcClientProperties;
import io.grpc.Attributes;
import io.grpc.NameResolver;
import io.grpc.NameResolverProvider;
import io.grpc.internal.GrpcUtil;

public class AddressChannelResolverFactory extends NameResolverProvider {

    private final GrpcClientProperties properties;

    public AddressChannelResolverFactory(GrpcClientProperties properties) {
        this.properties = properties;
    }

    @Nullable
    @Override
    public NameResolver newNameResolver(URI targetUri, Attributes params) {
        return new AddressChannelNameResolver(targetUri.toString(), properties, params, GrpcUtil.SHARED_CHANNEL_EXECUTOR);
    }

    @Override
    public String getDefaultScheme() {
        return "address";
    }

    @Override
    protected boolean isAvailable() {
        return true;
    }

    @Override
    protected int priority() {
        return 5;
    }
}
