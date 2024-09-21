package com.zheng.travel.client.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMesageStatusVo implements java.io.Serializable {
    // 需要一个消息id
    private String messageId;
}