package com.zheng.travel.client.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class TravelUserSettingVo implements java.io.Serializable{
    // 用户ID
    private Long userId;
    // 用户头像
    private String avatar;
    // 存储桶的名称
    private String bucketname;
    // 背景图片
    private String bgImg;
    // 昵称
    private String nickname;
    // 描述
    private String description;
    // 性别
    private Integer sex;
}
