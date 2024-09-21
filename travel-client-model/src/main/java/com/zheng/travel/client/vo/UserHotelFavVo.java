package com.zheng.travel.client.vo;

import lombok.Data;

@Data
public class UserHotelFavVo implements java.io.Serializable {

    // 收藏的而用户
    private Long userId;
    // 收藏的酒店
    private Long hotelId;
}
