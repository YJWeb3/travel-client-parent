package com.zheng.travel.client.message;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class MessageMQApplication {
    public static void main(String[] args) {
        SpringApplication.run(MessageMQApplication.class);
    }
}
