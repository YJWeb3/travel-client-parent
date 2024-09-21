package com.zheng.travel.client.vo;

import lombok.Data;

@Data
public class UserHotelFavSearchVo implements java.io.Serializable {

    private Integer pageNo = 1;
    private Integer pageSize = 10;
    private String keyword;

}
