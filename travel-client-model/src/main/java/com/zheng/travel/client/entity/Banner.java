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
@TableName("travel_banner")
public class Banner implements java.io.Serializable {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private String title;
    // 调整地址i
    private String hreflink;
    // 打开的类型：navigateto, redirect,switchtab
    private String opentype;
    // 描述
    private String description;
    private String img;
    private Integer sorted;
    private Integer status;
    private Integer isdelete;
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
}
