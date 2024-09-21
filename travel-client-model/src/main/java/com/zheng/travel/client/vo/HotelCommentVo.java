package com.zheng.travel.client.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HotelCommentVo implements java.io.Serializable {

    // 父id
    private Long parentId;
    // 评论的内容
    private String content;
    //	酒店
    private Long hotelId;
}
