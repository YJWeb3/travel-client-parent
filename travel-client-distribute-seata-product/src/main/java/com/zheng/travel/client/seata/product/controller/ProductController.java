package com.zheng.travel.client.seata.product.controller;

import com.zheng.travel.client.seata.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/deduct")
    public Boolean deduct(@RequestParam Long productId,
                          @RequestParam Integer count) {
        productService.deduct(productId, count);
        return true;

    }
}
