package com.zheng.travel.client.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserOrderBo implements java.io.Serializable{

    private Long id;    //主健
    private String orderno;    //订单编号
    private Long userId;    //下单用户id
    private String nickname;    //	下单用户的昵称
    private String avatar;    //	下单用户的头像
    private Integer status;    //	订单状态 -1 失效的订单 0 未支付的订单 1 已支付 2已完成
    private Date createTime;    //	创建时间
    private Date updateTime;    //	更新时间
    private String paymoney;    //	下单金额

}
