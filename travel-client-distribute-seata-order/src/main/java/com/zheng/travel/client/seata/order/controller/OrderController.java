package com.zheng.travel.client.seata.order.controller;

import com.zheng.travel.client.seata.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/create")
    public Boolean create(@RequestParam Integer count) {
        try {
            return orderService.create(count);
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }
}
