package com.zheng.travel.client.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@TableName("travel_hotel_comment_user_zans")
public class HotelommentUserZan implements java.io.Serializable {

    //	主健
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    //评论用户id
    private Long userid;
    //	评论的层级0第一级 1第二级
    private Long commentId;
    /**
     * 创建时间 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
}
