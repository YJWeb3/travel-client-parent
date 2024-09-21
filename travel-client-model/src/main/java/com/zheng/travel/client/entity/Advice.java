package com.zheng.travel.client.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("travel_msg_advice")
public class Advice implements java.io.Serializable {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private String title;
    private Integer sorted;
    private Integer status;
    // 打开的类型： redirect,navigate，switchTab
    private String opentype;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    //跳转地址
    private String linkhref;
    // 删除状态
    private Integer isdelete;

}
