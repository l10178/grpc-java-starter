package com.nxest.grpc.spring.test.client;

import com.nxest.grpc.spring.client.GrpcChannelFactory;
import com.nxest.grpc.spring.client.GrpcClient;
import com.nxest.grpc.spring.test.GreeterGrpc;
import com.nxest.grpc.spring.test.HelloRequest;
import com.nxest.grpc.spring.test.HelloResponse;
import io.grpc.Channel;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class HelloWorldClient {

    @GrpcClient
    private Channel channel;

    @Resource
    private GrpcChannelFactory grpcChannelFactory;

    private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    @PostConstruct
    public void start() {
        executorService.scheduleWithFixedDelay(this::test, 2, 2, TimeUnit.SECONDS);
        Channel aDefault = grpcChannelFactory.createChannel("default");
        System.out.println(aDefault);

    }

    private void test() {
        try {
            //bug channel may be null
            GreeterGrpc.GreeterBlockingStub stub = GreeterGrpc.newBlockingStub(channel);

            HelloResponse helloResponse = stub.sayHello(
                HelloRequest.newBuilder()
                    .setName("nxest")
                    .build());

            System.out.println(helloResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
