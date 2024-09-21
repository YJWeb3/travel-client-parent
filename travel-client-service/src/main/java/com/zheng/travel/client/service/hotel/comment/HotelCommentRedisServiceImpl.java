package com.zheng.travel.client.service.hotel.comment;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zheng.travel.client.redis.TravelRedisOperator;
import com.zheng.travel.client.entity.HotelComment;
import com.zheng.travel.client.entity.TravelUser;
import com.zheng.travel.client.mapper.hotel.HotelCommentMapper;
import com.zheng.travel.client.threadlocal.UserThrealLocal;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class HotelCommentRedisServiceImpl extends ServiceImpl<HotelCommentMapper, HotelComment> implements IHotelCommentService {


    @Autowired
    private TravelRedisOperator redisOperator;
    @Autowired
    private IHotelCommentUserZanServiceImpl hotelommentUserZanService;


    /**
     * 评论赞+1
     * @param commentId
     * @return
     */
    @Override
    @Transactional
    public int updateCommentPlusZannum(@Param("commentId")Long commentId){
        TravelUser TravelUser = UserThrealLocal.get();
        //1: 评论表zannum + 1
        redisOperator.incrementHash("redis:user:comment:like",commentId+"",1L);
        redisOperator.setHashValue("redis:comment:like:user", TravelUser.getId() + ":" + commentId, "1");
        return 1;
    }

    /**
     * 评论赞 - 1
     * @param commentId
     * @return
     */
    @Override
    @Transactional
    public int updateCommentMiusZannum(@Param("commentId")Long commentId){
        TravelUser TravelUser = UserThrealLocal.get();
        //1: 评论表zannum + 1
        redisOperator.decrementHash("redis:user:comment:like",commentId+"",1L);
        redisOperator.hdel("redis:comment:like:user", TravelUser.getId() + ":" + commentId);
        return 1;
    }
}
