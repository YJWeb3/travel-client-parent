package com.zheng.travel.client.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HotelTypeBo implements java.io.Serializable{
    private Long id;
    //	标题
    private String title;
    //	封面图
    private String img;
    //	图集
    private String imglists;
    private List<String> imageLists;
    //	描述
    private String description;
    //	房型价格
    private String price;
    //	房型成交价格
    private BigDecimal realprice;
    //	酒店
    private Long hotelId;
}
