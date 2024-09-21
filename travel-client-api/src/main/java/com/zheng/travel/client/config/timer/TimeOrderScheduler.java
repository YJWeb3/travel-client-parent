package com.zheng.travel.client.config.timer;

import com.zheng.travel.client.service.userorder.IUserOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TimeOrderScheduler {


    @Autowired
    private IUserOrderService userOrderService;

    @Scheduled(cron = "0/10 * * * * ? ")
    public void ordertask(){
        log.info("进行订单的状态的查询和比较....");
        userOrderService.findOrderNoPay();
    }

}
