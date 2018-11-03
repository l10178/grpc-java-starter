package com.nxest.grpc.spring.client;

import io.grpc.ClientInterceptor;

import java.util.Collection;

public interface GrpcClientInterceptorDiscoverer {
    Collection<ClientInterceptor> findGrpcClientInterceptors(GrpcClient annotation);
}
