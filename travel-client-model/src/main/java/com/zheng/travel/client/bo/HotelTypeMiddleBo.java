package com.zheng.travel.client.bo;

import lombok.Data;

import java.math.BigDecimal;


@Data
public class HotelTypeMiddleBo implements java.io.Serializable {

    // 主健
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
