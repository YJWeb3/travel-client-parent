package com.zheng.travel.client.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@TableName("travel_hotel_comment")
public class HotelComment implements java.io.Serializable {

    //	主健
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    //评论内容
    private String content;
    //评论用户id
    private Long userid;
    //评论用户昵称
    private String usernickname;
    //评论用户头像
    private String useravatar;
    //	回复用户id
    private Long replyUserid;
    //回复用户昵称
    private String replyUsernickname;
    //回复用户头像
    private String replyUseravatar;
    //	评论的层级0第一级 1第二级
    private Long parentId;
    //	酒店
    private Long hotelId;
    //	订单评论
    private Long orderId;
    //	点赞数
    private Integer zannum;
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
