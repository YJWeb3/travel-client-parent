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
@TableName("travel_hotel_category")
public class HotelCategory implements java.io.Serializable {
    //	主健
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    //名称
    private String title;
    //描述
    private String description;
    //	发布状态 0未发布 1发布
    private Integer status;
    //排序
    private Integer sorted;
    //删除状态
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
