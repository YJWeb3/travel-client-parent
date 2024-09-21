package com.zheng.travel.client.redisson.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
public class ConsummerController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/getordernum")
    @ResponseBody
    public String orderNumber() {
        return stringRedisTemplate.opsForValue().get("ordernum");
    }
}
