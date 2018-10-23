package com.nxest.grpc.spring.test;

import org.springframework.stereotype.Service;

@Service
public class HelloWorldResource {

    public void sayTest(){
        System.out.println("this is test by spring");
    }
}
