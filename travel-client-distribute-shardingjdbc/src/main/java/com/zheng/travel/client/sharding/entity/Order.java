package com.zheng.travel.client.sharding.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_order")
public class Order implements java.io.Serializable{

    //@TableId(type = IdType.ASSIGN_ID)
    private Long orderId;
    // 下单用户
    private Integer userId;
    // 下单用户
    private Date createTime;
}
