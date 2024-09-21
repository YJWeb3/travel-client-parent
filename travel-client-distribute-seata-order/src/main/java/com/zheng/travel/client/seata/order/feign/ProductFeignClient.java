package com.zheng.travel.client.seata.order.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "seata-product", url = "http://localhost:8086")
public interface ProductFeignClient {

    @GetMapping("/deduct")
    Boolean deduct(@RequestParam Long productId,
                   @RequestParam Integer count);
}
