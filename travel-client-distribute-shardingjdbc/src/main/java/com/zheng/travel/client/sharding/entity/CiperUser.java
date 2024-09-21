package com.zheng.travel.client.sharding.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_cipher_old")
public class CiperUser implements java.io.Serializable {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    // 用户昵称
    private String name;
    // 创建时间
    private Date createTime;
    // 更新时间
    private Date updateTime;
    // 密码
    private String pwd;
    // 手机
    private String mobile;
}
