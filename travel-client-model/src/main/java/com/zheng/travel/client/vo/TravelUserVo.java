package com.zheng.travel.client.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class TravelUserVo implements java.io.Serializable{

    @NotNull(message = "请输入手机号码")
    @Length(max = 11,min = 11,message = "请输入11位手机号码")
    @ApiModelProperty(value = "手机号码")
    @Pattern(regexp = "(^0?1[1|2|3|4|5|7|6|8|9][0-9]\\d{8}$)",message = "请输入正确的手机号码")
    private String phone;

    @ApiModelProperty(value = "短信码")
    @NotEmpty(message = "请输入短信码")
    private String smscode;

    /*用于消息处理*/
    private String nickname;
    private String avatar;
    private String id;

}
