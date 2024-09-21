package com.zheng.travel.client.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageVo implements java.io.Serializable {

    // 分页信息
    private Integer pageNo;
    private Integer pageSize;
    // 查询用户信息
    private String userId;
    // 查询消息类型 1 我的消息 2 订单消息 3 系统消息（针对所有用户）
    private Integer msgtype;
}
