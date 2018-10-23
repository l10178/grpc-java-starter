package com.nxest.grpc.spring.test.client;

import com.nxest.grpc.GreeterGrpc;
import com.nxest.grpc.HelloRequest;
import com.nxest.grpc.HelloResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class HelloWorldClient {


    public static void main(String[] args) throws InterruptedException {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 6500)
            .usePlaintext()
            .build();

        GreeterGrpc.GreeterBlockingStub stub =
            GreeterGrpc.newBlockingStub(channel);

        HelloResponse helloResponse = stub.sayHello(
            HelloRequest.newBuilder()
                .setName("Ray")
                .build());

        System.out.println(helloResponse);

        channel.shutdown();
    }
}
