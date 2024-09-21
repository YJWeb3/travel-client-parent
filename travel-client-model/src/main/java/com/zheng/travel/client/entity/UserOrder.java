package com.zheng.travel.client.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@TableName("travel_user_order_hotel")
public class UserOrder implements java.io.Serializable {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;    //主健
    private Long userId;    //下单用户id
    private String nickname;    //	下单用户的昵称
    private String avatar;    //	下单用户的头像

    private Long hotelId;//酒店id
    private String hotelImg;//酒店封面
    private String hotelTitle;//酒店标题
    private String hotelDesc;//酒店描述
    private String hotelAddress;//酒店地址
    private String hotelTel;//酒店联系方式
    private String hotelLan;//酒店经度
    private String hotelLgt;//酒店纬度
    private String hotelDetailTitle;//酒店房型标题
    private String hotelDetailImg;//酒店房型封面
    private Long hotelDetailId;//酒店房型id
    private String commentInfo;    //评论信息
    private String orderno;    //订单编号 微信支付和阿里支付 用来退款
    private String tradeno;//交易号，微信支付和阿里支付 用来退款
    private String openid;// 用户Openid
    private String paymoney;//	下单金额
    private String ip;//	支付IP地址
    private String ipaddress;//	支付用户的IP具体地址
    private Integer paytype;//	支付方式 1 微信支付 2 支付支付 3 待定
    private Integer days;// 入住天数

    private Integer status;    //	订单状态 -1 失效的订单 0 未支付的订单 1 已支付 2已完成 3 已完成
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;    //	创建时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;    //	更新时间
    // 支付的酒店房型
    private String startDate;
    // 支付的酒店房型
    private String endDate;
    // 支付的酒店房型
    private String phone;
    // 支付的酒店房型
    private Integer hnum;
    // 支付的酒店房型
    private Integer isdelete;
    // 其他消息
    private String dinfo;
}
