package com.nxest.grpc.spring.test.client;

import com.nxest.grpc.spring.client.GrpcClient;
import com.nxest.grpc.spring.test.GreeterGrpc;
import com.nxest.grpc.spring.test.HelloRequest;
import com.nxest.grpc.spring.test.HelloResponse;
import io.grpc.Channel;
import org.springframework.stereotype.Service;

@Service
public class GrpcClientService {

    @GrpcClient
    private Channel channel;

    @GrpcClient
    private GreeterGrpc.GreeterBlockingStub stub;

    public void testChaanelMessage() {
        GreeterGrpc.GreeterBlockingStub stub = GreeterGrpc.newBlockingStub(channel);

        HelloResponse helloResponse = stub.sayHello(
            HelloRequest.newBuilder()
                .setName("test channel message")
                .build());
        System.out.println(helloResponse);
    }

    public void testStubMessage() {
        HelloResponse helloResponse = stub.sayHello(
            HelloRequest.newBuilder()
                .setName("test stub message")
                .build());
        System.out.println(helloResponse);
    }
}
