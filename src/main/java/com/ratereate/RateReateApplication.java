package com.ratereate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients()
public class RateReateApplication {

    public static void main(String[] args) {
        SpringApplication.run(RateReateApplication.class, args);
    }

}
