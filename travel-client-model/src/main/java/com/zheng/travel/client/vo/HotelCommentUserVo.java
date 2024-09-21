package com.zheng.travel.client.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HotelCommentUserVo implements java.io.Serializable {

    // 评论id
    private Long commentId;
    //用户id
    private Long userId;
}
