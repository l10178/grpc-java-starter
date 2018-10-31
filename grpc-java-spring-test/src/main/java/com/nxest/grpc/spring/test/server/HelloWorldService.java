package com.nxest.grpc.spring.test.server;


import com.nxest.grpc.spring.server.GrpcService;
import com.nxest.grpc.spring.test.GreeterGrpc;
import com.nxest.grpc.spring.test.HelloRequest;
import com.nxest.grpc.spring.test.HelloResponse;
import io.grpc.stub.StreamObserver;

import javax.annotation.Resource;

@GrpcService
public class HelloWorldService extends GreeterGrpc.GreeterImplBase {

    //test resource
    @Resource
    private HelloWorldResource helloWorldResource;

    @Override
    public void sayHello(HelloRequest req, StreamObserver<HelloResponse> responseObserver) {
        helloWorldResource.sayTest();
        HelloResponse reply = HelloResponse.newBuilder().setMessage("Hello " + req.getName()).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}
