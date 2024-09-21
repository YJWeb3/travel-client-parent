package com.zheng.travel.client.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderPayVo implements java.io.Serializable {
    // 支付的用户
    private String openid;
    // 支付的酒店房型
    private String hotelDetailId;
    // 订单编号
    private String orderno;
    // 入住事件
    private String startDate;
    // 离店事件
    private String endDate;
    // 入住用户的姓名
    private String nickname;
    // 入住用户的联系方式
    private String phone;
    // 入住的数量
    private Integer hnum;
    // 其他消息
    private String dinfo ;
}
