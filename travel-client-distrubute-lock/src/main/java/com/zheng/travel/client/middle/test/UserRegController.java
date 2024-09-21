package com.zheng.travel.client.middle.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


@RestController
public class UserRegController {

    @Autowired
    private MessageService messageService;
    @Autowired
    private RegService regService;


    @PostMapping("/reg/user")
    public String reguser() {
        new Thread(messageService).start();
        new Thread(regService).start();
        return "success";
    }
}
