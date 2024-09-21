package com.zheng.travel.client.seata.product.service;

import com.zheng.travel.client.seata.product.entity.ProductEntity;
import com.zheng.travel.client.seata.product.product.ProductDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class ProductService {

    @Autowired
    private ProductDao productDao;

    public void deduct(Long productId, Integer count) {
        log.info("开始扣库存. productId={}, count={}", productId, count);
        Optional<ProductEntity> byId = productDao.findById(productId);
        if (byId.isPresent()) {
            ProductEntity entity = byId.get();
            entity.setCount(entity.getCount() - count);
            productDao.save(entity);
        }
    }
}
