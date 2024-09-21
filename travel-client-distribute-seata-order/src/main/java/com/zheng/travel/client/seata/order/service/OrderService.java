package com.zheng.travel.client.seata.order.service;

import com.zheng.travel.client.seata.order.feign.ProductFeignClient;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderService {

    //Feign
    @Autowired
    private ProductFeignClient productFeignClient;

    @GlobalTransactional
    public Boolean create(Integer count) {
        Boolean result = productFeignClient.deduct(5001L, count);
        if (result != null && result) {
            //可能抛出异常
            if (5 == count) {
                throw new RuntimeException("order发生异常了");
            }

            log.info("数据库开始创建订单");
            return true;
        }

        return false;
    }
}
