package com.zheng.travel.client.controller;


import com.zheng.travel.client.constant.TravelContants;
import com.zheng.travel.client.redis.TravelRedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BaseController implements TravelContants {

    @Autowired
    protected TravelRedisOperator redisOperator;
}
