package com.hotdms.proxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.hotdms.proxy.client.SSLClient;

@SpringBootApplication
public class Application

{
    public static void main( String[] args ) throws Exception {
    	SSLClient.initialize();
        SpringApplication.run(Application.class, args);
    }
}