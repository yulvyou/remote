package com.example.remote;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class RemoteApplication {

    public static void main(String[] args) {
        SpringApplication.run(RemoteApplication.class, args);
//        log.info("hello,remoteApplication");
    }
}
