package com.zheng.travel.client.service.weixin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class WeixinData implements java.io.Serializable {
    // 用户id
    private Long userid;
    // 微信昵称
    private String nickName;
    // 微信性别
    private String gender;
    // 微信用户的省市区
    private String country;
    private String province;
    private String city;
    // 微信用户语言
    private String language;
    // 微信用户头像
    private String avatarUrl;
    // 微信用户的openid
    private String openid;
    // 手机信息私密信息
    private String encrypted;
    private String iv;
    // 微信用户的登录code
    private String code;
    //unionid
    private String unionid;
    // 用来换取数据使用的
    private String sessionkey;
    // 微信用户的手机号码
    private String phonenumber;
    //token
    private String token;
}
