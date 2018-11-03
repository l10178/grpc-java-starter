package com.nxest.grpc.test.client;

import com.nxest.grpc.client.GrpcClient;
import com.nxest.grpc.test.GreeterGrpc;
import com.nxest.grpc.test.HelloRequest;
import com.nxest.grpc.test.HelloResponse;
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
