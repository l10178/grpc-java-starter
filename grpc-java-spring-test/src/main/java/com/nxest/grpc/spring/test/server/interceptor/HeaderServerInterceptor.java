package com.nxest.grpc.spring.test.server.interceptor;

import com.nxest.grpc.spring.server.GrpcServerInterceptor;
import io.grpc.ForwardingServerCall.SimpleForwardingServerCall;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;

/**
 * A interceptor to handle server header.
 */
@GrpcServerInterceptor
public class HeaderServerInterceptor implements ServerInterceptor {


    static final Metadata.Key<String> CUSTOM_HEADER_KEY =
        Metadata.Key.of("custom_server_header_key", Metadata.ASCII_STRING_MARSHALLER);


    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
        ServerCall<ReqT, RespT> call,
        final Metadata requestHeaders,
        ServerCallHandler<ReqT, RespT> next) {
        System.out.println("header received from client:" + requestHeaders);
        return next.startCall(new SimpleForwardingServerCall<ReqT, RespT>(call) {
            @Override
            public void sendHeaders(Metadata responseHeaders) {
                responseHeaders.put(CUSTOM_HEADER_KEY, "customRespondValue");
                super.sendHeaders(responseHeaders);
            }
        }, requestHeaders);
    }
}
