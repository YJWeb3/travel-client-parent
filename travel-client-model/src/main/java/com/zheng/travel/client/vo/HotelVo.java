package com.zheng.travel.client.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HotelVo implements java.io.Serializable {

    // 起始页
    private Integer pageNo = 1;
    // 记录数
    private Integer pageSize = 10;
    // 距离
    private Integer distanct;

    // 价格范围开始价格
    private Integer startPrice;
    // 价格微微的结束价格
    private Integer endPrice;
    // 区域编号
    private String areacode;
    // 品牌
    private Long brandId;
    // 分类
    private Long categoryId;
    // 星级
    private Integer starlevel;
    // 关键字搜索
    private String keyword ;

}
