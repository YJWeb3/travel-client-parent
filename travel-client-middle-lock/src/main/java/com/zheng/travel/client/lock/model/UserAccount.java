package com.zheng.travel.client.lock.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * 用户账户实体
 */
@Data
@ToString
@TableName("user_account")
public class UserAccount {
    @TableId(type = IdType.AUTO)
    private Integer id; //主键Id
    private Integer userId;//用户账户id
    private BigDecimal amount;//账户余额
    private Integer version; //版本号
    private Byte isActive; //是否有效账户
}