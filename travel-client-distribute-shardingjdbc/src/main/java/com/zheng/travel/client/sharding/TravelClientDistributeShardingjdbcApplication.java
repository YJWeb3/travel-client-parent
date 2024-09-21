package com.zheng.travel.client.sharding;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.zheng.travel.client.sharding.mapper")
public class TravelClientDistributeShardingjdbcApplication {

    public static void main(String[] args) {
        SpringApplication.run(TravelClientDistributeShardingjdbcApplication.class, args);
    }

}
