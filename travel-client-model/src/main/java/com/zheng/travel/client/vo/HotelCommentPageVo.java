package com.zheng.travel.client.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HotelCommentPageVo implements java.io.Serializable {

    // 起始页
    private Integer pageNo = 1;
    // 记录数
    private Integer pageSize = 10;
    // 查询某个酒店的评论
    private Long hotelId;
    // 查询对应的评论层级
    private Long parentId;
}
