package com.nxest.grpc.test.server;


import com.nxest.grpc.server.GrpcService;
import com.nxest.grpc.test.GreeterGrpc;
import com.nxest.grpc.test.HelloRequest;
import com.nxest.grpc.test.HelloResponse;
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
