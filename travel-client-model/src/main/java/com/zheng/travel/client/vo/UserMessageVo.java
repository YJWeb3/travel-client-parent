package com.zheng.travel.client.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMessageVo implements java.io.Serializable {

    // 分页信息
    private Integer pageNo;
    private Integer pageSize;
    // 查询用户信息
    private String userId;
}
