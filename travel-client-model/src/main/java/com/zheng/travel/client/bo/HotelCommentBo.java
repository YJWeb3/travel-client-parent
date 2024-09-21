package com.zheng.travel.client.bo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class HotelCommentBo implements java.io.Serializable {

    //	主健
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
    //	点赞数
    private Integer zannum;
    //	用户是否点赞 1点赞过 0未点赞
    private Integer iszan;
    // 评论数
    private Integer comments;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 二级评论
     */
    private Page<HotelCommentBo> pageChildrens;
}
