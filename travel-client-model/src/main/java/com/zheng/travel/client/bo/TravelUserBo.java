package com.zheng.travel.client.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TravelUserBo implements java.io.Serializable{

    private Long id;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 昵称，媒体号
     */
    private String nickname;

    /**
     * 类似头条号，抖音号，公众号，唯一标识，需要限制修改次数，比如终生1次，每年1次，每半年1次等，可以用于付费修改。
     */
    private String idcode;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 性别 1:男  0:女  2:保密
     */
    private Integer sex;

    /**
     * 生日
     */
    private Date birthday;

    /**
     * 国家
     */
    private String country;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 区县
     */
    private String district;

    /**
     * 简介
     */
    private String description;

    /**
     * 个人介绍的背景图
     */
    private String bgImg;

    /**
     * 创建时间
      */
    private Date createTime;

    /**
     * token
     */
    private String token;
    /**
     * sessionkey
     */
    private String sessionkey;
    /**
     * openid
     */
    private String openid;
    /**
     * unionid
     */
    private String unionid;
}
