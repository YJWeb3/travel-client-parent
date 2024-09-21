package com.zheng.travel.client.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserOrderCommentVo implements java.io.Serializable {
    // 品牌
    private Long orderId;
    // 关键字搜索
    private String comment ;

}
