package com.zheng.travel.client.service.hotel.comment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zheng.travel.client.bo.HotelCommentBo;
import com.zheng.travel.client.service.BaseService;
import com.zheng.travel.client.entity.HotelComment;
import com.zheng.travel.client.vo.HotelCommentPageVo;
import com.zheng.travel.client.vo.HotelCommentVo;

public interface IHotelCommentService extends IService<HotelComment> , BaseService {


    /**
     * 保存酒店评论
     * @param hotelCommentPageVo
     * @return
     */
    default Page<HotelCommentBo> loadHotelComment(HotelCommentPageVo hotelCommentPageVo){return null;}

    /**
     * 查询酒店评论信息
     * @param hotelId
     * @param parentId
     * @param pageNo
     * @param pageSize
     * @return
     */
    default Page<HotelCommentBo> loadCommentChildren(Long hotelId, Long parentId, Integer pageNo, Integer pageSize){return null;}



    /**
     * 保存酒店评论
     * @param hotelCommentVo
     * @return
     */
    default HotelCommentBo saveHotelComment(HotelCommentVo hotelCommentVo){return null;}

    /**
     * 保存酒店评论
     * @param commentId 评论的主健
     * @param userId 用户ID 考虑到一个安全性的问题
     * @return
     */
    default boolean delHotelComment(Long hotelId,Long commentId, Long userId){return false;}


    /**
     * 评论赞+1
     * @param commentId
     * @return
     */
    int updateCommentPlusZannum(Long commentId);

    /**
     * 评论赞 - 1
     * @param commentId
     * @return
     */
    int updateCommentMiusZannum(Long commentId);


    /**
     * 根据酒店ID查询评论数
     * @param hotelId
     * @return
     */
   default int countHotelComment(Long hotelId){return 0;}

}
