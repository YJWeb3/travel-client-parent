package com.zheng.travel.client.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVo implements java.io.Serializable {

    /**
     * 订单编号
     */
    @NotEmpty(message = "请输入下单的编号")
    private String orderNo;
    /**
     * 下单的用户 这个是从后台获取
     */
    private Long userId;
}
