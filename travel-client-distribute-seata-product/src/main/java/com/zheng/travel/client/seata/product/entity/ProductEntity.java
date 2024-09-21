package com.zheng.travel.client.seata.product.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "kss_product")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    private Integer count;
}
