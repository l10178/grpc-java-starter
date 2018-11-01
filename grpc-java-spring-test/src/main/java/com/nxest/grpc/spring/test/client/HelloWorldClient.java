package com.nxest.grpc.spring.test.client;

import io.grpc.Channel;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class HelloWorldClient {

    @Resource
    private GrpcClientService clientService;

    private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    @PostConstruct
    public void start() {
        executorService.scheduleWithFixedDelay(this::test, 2, 2, TimeUnit.SECONDS);

    }

    private void test() {
        clientService.testChaanelMessage();
        clientService.testStubMessage();
    }
}
