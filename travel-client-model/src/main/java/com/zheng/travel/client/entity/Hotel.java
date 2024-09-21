package com.zheng.travel.client.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("travel_hotel")
public class Hotel implements java.io.Serializable {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    // 酒店名称
    private String title;
    // 酒店封面图
    private String img;
    // 酒店图集
    private String imglists;
    // 酒店的描述
    private String description;
    // 酒店的活动价格
    private String price;
    // 酒店的真实价格
    private BigDecimal realprice;
    // 酒店的发布状态0未发布1发布
    private Integer status;
    // 酒店的删除状态0未删除1删除
    private Integer isdelete;
    // 浏览数
    private Integer views;
    // 收藏数
    private Integer collects;
    // 评论数
    private Integer comments;
    // 购买数
    private Integer buynum;
    // 酒店的经度
    private String lan;
    // 酒店的纬度
    private String lgt;
    // 酒店的国家
    private String contury;
    // 酒店的省份
    private String province;
    // 省份编号
    private String provincecode;
    // 酒店的城市
    private String city;
    // 城市编号
    private String citycode;
    // 酒店的区域
    private String area;
    // 酒店区域编号
    private String areacode;
    // 是否正常营业 0 打烊了 1 营业中 (定时器开发)
    private Integer isopen;
    // 酒店的具体地址
    private String address;
    // 酒店的座机电话
    private String phone;
    // 酒店的星级
    private Integer starlevel;
    // 酒店的分类
    private Long hotelCategoryId;
    // 酒店的标签
    private String tags;
    // 酒店的品牌
    private Long hotelBrandId;
    // 酒店的品牌名称
    private String hotelBrandName;
    // 酒店分类对应的名字
    private String hotelCategoryName;
    // 酒店的公告
    private String advice;
    /*其他相关信息*/
    private String relationinfo;
    /*服务项*/
    private String hotelServiceItem;
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
