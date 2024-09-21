package com.zheng.travel.client.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class TravelLogoutVo implements java.io.Serializable{
    private String token;
    private String userId;
}
