package com.zheng.travel.client.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("travel_user_fav_hotel")
public class UserFavHotel implements java.io.Serializable {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    // 收藏用户
    private Long userId;
    // 收藏的酒店
    private Long hotelId;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

}
