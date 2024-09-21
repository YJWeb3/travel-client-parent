package com.zheng.travel.client.mapper.hotel;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zheng.travel.client.entity.HotelComment;
import org.apache.ibatis.annotations.Param;

public interface HotelCommentMapper extends BaseMapper<HotelComment> {


    /**
     * 评论赞+1
     * @param commentId
     * @return
     */
    int updateCommentPlusZannum(@Param("commentId")Long commentId);

    /**
     * 评论赞 - 1
     * @param commentId
     * @return
     */
    int updateCommentMiusZannum(@Param("commentId")Long commentId);
}
