package com.zheng.travel.client.lock.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("user_reg")
public class UserReg implements java.io.Serializable{

    @TableId(type = IdType.AUTO)
    private Integer id; //用户id
    private String userName; //用户名
    private String password; //密码
    private Date createTime; //创建时间
}
