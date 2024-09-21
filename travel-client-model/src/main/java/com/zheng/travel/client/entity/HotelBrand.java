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
@TableName("travel_hotel_brand")
public class HotelBrand implements java.io.Serializable {
    //	主健
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    //	品牌的名称
    private String title;
    //品牌的描述
    private String description;
    //品牌的星级
    private Integer starlevel;
    //	发布状态 0未发布 1发布
    private Integer status;
    //排序
    private Integer sorted;
    //删除状态
    private Integer isdelete;
    //品牌的logo
    private String logo;
    //品牌的地址
    private String address;
    //品牌的官网
    private String netlink;
    //品牌的联系方式
    private String telephone;
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
