package com.nxest.grpc.spring.server;

import java.util.Collection;

public interface GrpcServiceDiscoverer {
    Collection<GrpcServiceDefinition> findGrpcServices();
}
