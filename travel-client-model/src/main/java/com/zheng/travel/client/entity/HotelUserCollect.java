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
@TableName("travel_user_collect_hotel")
public class HotelUserCollect implements java.io.Serializable {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 关注酒店
     */
    private Long hotelid;

    /**
     * 关注用户
     */
    private Long userid;

    /**
     * 更新状态 1 关注 0未关注
     */
    private Integer status;

    /**
     * 创建时间 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

}
