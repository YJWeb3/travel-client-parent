package com.zheng.travel.client.lock.controller.zk;

import com.zheng.travel.client.lock.common.R;
import com.zheng.travel.client.lock.common.StatusCode;
import com.zheng.travel.client.lock.service.zk.IProductService;
import com.zheng.travel.client.lock.vo.ProductVo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "zk分布式锁- 商品抢购扣减库存")
public class ProductController {

    @Autowired
    private IProductService productService;


    @GetMapping("/purchase/product")
    public R purchaseProduct(ProductVo productVo) {
        if (StringUtils.isEmpty(productVo.getProductNo())) {
            return new R(701, "商品找不到!!!");
        }

        if (StringUtils.isEmpty(productVo.getUserId())) {
            return new R(701, "找不到用户!!!");
        }

        R response = new R(StatusCode.Success);

        try {
            // 不加锁锁商品抢购
            //productService.purchaseProductNoLock(productVo);

            // zookeeper的分布式锁
            //productService.purchaseProductZKLock(productVo);
            // redisson的分布式锁
            productService.purchaseProductRedissonLock(productVo);
        }catch ( Exception ex){
            response = new R(StatusCode.Fail.getCode(),ex.getMessage());
        }

        return response;
    }


}

