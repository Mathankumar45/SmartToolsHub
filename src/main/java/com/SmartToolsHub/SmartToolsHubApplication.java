package com.SmartToolsHub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SmartToolsHubApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartToolsHubApplication.class, args);
    }
}