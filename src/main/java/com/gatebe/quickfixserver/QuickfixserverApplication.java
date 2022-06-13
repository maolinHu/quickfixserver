package com.gatebe.quickfixserver;

import io.allune.quickfixj.spring.boot.starter.EnableQuickFixJServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableQuickFixJServer
@SpringBootApplication
public class QuickfixserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuickfixserverApplication.class, args);
    }

}
