package com.zheng.travel.client.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("travel_user_message_order")
public class UserMesageStatus implements java.io.Serializable {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    // 读的消息
    private String messageId;
    // 用户
    private Long userId;
    /**
     * 创建时间 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
