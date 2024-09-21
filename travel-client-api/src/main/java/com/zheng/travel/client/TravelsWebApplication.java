package com.zheng.travel.client;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@MapperScan("com.zheng.travel.client.mapper")
@EnableCaching
@EnableMongoRepositories
@EnableAsync //开启注解
//@EnableScheduling
public class TravelsWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(TravelsWebApplication.class);
    }

}
