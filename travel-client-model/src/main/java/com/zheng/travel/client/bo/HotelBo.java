package com.zheng.travel.client.bo;

import com.zheng.travel.client.entity.HotelTypeMiddle;
import lombok.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HotelBo implements java.io.Serializable {

    private Long id;
    // 酒店名称
    private String title;
    // 酒店封面图
    private String img;
    // 酒店图集
    private String imglists;
    /**
     * 酒店图集
     */
    private List<String> imagesList;
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
    // 购买数
    private Integer buynum;
    // 购买数
    private Integer comments;
    // 酒店的经度
    private String lan;
    // 酒店的纬度
    private String lgt;
    // 酒店的国家
    private String contury;
    // 酒店的具体地址
    private String address;
    // 酒店的座机电话
    private String phone;
    // 酒店的城市
    private String city;
    // 酒店的区域
    private String area;
    // 酒店的省份
    private String province;
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
    /*服务项*/
    private String hotelServiceItem;
    /*其他相关信息*/
    private String relationinfo;
    /*服务项*/
    private List<Map<String, Object>> serviceItems;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 评论的总数
     */
    private String totalComments;
    // 对应房型列表
    private List<HotelTypeMiddle> hotelTypes;

    // 是否已经收藏
    private Boolean isCollect = false;
}
