package com.zheng.travel.client.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("travel_hotel_type")
public class HotelType  implements java.io.Serializable {
    // 主健
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    // 标题
    private String title;
    // 封面图
    private String img;
    // 描述
    private String description;
    // 创建时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    // 更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    // 发布状态1发布0未发布
    private Integer status;
    // 排序字段
    private Integer sorted;
    // 删除状态
    private Integer isdelete;
}