package com.zheng.travel.client.seata.product.product;

import com.zheng.travel.client.seata.product.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDao extends JpaRepository<ProductEntity, Long> {
}
