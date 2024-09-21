package com.zheng.travel.client.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class TravelUserPwdVo implements java.io.Serializable{

    @NotEmpty(message = "请输入手机号码")
    @ApiModelProperty(value = "手机号码")
    private String phone;

    @ApiModelProperty(value = "密码")
    @NotEmpty(message = "请输入密码")
    private String password;
}
