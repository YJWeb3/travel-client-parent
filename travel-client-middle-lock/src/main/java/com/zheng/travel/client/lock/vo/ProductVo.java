package com.zheng.travel.client.lock.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProductVo implements Serializable {

    // 抢购的用户, 建议在后端获取
    private Integer userId;
    // 抢购商品的编号
    private String productNo;
    // 抢购商品的id
    //private String productId;
}
