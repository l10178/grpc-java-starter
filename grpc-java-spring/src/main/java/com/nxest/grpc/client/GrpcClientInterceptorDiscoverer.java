package com.nxest.grpc.client;

import io.grpc.ClientInterceptor;

import java.util.Collection;

public interface GrpcClientInterceptorDiscoverer {
    Collection<ClientInterceptor> findGrpcClientInterceptors(GrpcClient annotation);
}
