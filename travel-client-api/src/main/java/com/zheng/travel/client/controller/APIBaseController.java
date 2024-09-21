package com.zheng.travel.client.controller;


import com.zheng.travel.client.constant.TravelContants;
import com.zheng.travel.client.redis.TravelRedisOperator;
import com.zheng.travel.client.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
public class APIBaseController implements TravelContants, BaseService {
    @Autowired
    protected TravelRedisOperator redisOperator;
}
