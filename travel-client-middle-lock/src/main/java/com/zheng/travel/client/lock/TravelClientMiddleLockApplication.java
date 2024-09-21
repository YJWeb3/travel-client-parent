package com.zheng.travel.client.lock;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@MapperScan(basePackages = "com.zheng.travel.lock.mapper")
public class TravelClientMiddleLockApplication {

    public static void main(String[] args) {
        SpringApplication.run(TravelClientMiddleLockApplication.class, args);
    }

}
