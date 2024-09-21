package com.zheng.travel.client.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("travel_hotel_type_middle")
public class HotelTypeMiddle implements java.io.Serializable {
    // 主健
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    // 标题
    private String title;
    // 封面图
    private String img;
    // 描述
    private String description;
    // 房型价格
    private String price;
    // 房型成交价格
    private BigDecimal realprice;
    // 酒店
    private Long hotelId;
    // 酒店房型id
    private Long hotelTypeId;
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
    // 库存
    private Integer storenum;
    // 标签
    private String tags ;
    // 附属信息1
    private String finfo;
    // 附属信息2
    private String hinfo ;
}