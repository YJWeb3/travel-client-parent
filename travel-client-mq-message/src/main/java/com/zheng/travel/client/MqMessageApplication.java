package com.zheng.travel.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MqMessageApplication {
    public static void main(String[] args) {
        SpringApplication.run(MqMessageApplication.class);
    }
}
