package com.zheng.travel.client.lock.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户每次提现时金额记录实体
 */
@Data
@ToString
@TableName("user_account_record")
public class UserAccountRecord {
    @TableId(type = IdType.AUTO)
    private Integer id; //主键id
    private Integer accountId; //账户记录主键id
    private BigDecimal money; //提现金额
    private Date createTime; //提现成功时间
}