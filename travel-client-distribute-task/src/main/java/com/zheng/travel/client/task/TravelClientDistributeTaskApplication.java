package com.zheng.travel.client.task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
//@EnableScheduling
@EnableAsync
public class TravelClientDistributeTaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(TravelClientDistributeTaskApplication.class, args);
	}


}
