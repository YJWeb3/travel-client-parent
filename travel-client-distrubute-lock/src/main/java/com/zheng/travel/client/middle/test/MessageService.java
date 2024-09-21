package com.zheng.travel.client.middle.test;

import org.springframework.stereotype.Service;

@Service
public class MessageService implements Runnable{

    @Override
    public void run() {
        System.out.println("用户注册发消息");
    }
}
