package com.zheng.travel.client.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserOrderVo implements java.io.Serializable {

    // 起始页
    private Integer pageNo = 1;
    // 记录数
    private Integer pageSize = 10;
    // 关键字搜索
    private String keyword;
    // 支付状态 paystatus=null 全部订单 0 待支付订单 1 已支付订单 2 已完成 3 已评价
    private Integer paystatus;

}
