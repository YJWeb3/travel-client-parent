package com.zheng.travel.client.controller.hotel;

import com.zheng.travel.client.service.hotel.comment.IHotelCommentService;
import com.zheng.travel.client.controller.APIBaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@Api(tags = "酒店评论点赞")
public class HotelommentUserZanController extends APIBaseController {

    @Autowired
    @Qualifier("hotelCommentRedisServiceImpl")
    private IHotelCommentService hotelCommentService;

//    @Autowired
//    @Qualifier("hotelCommentServiceImpl")
//    private  IHotelCommentService hotelCommentService;


    /**
     * 用户点赞
     * @return
     */
    @ApiOperation("用户点赞")
    @PostMapping("/hotel/comment/like/{commentId}")
    public int usercommentlike(@PathVariable("commentId")Long commentId) {
        return hotelCommentService.updateCommentPlusZannum(commentId);
    }

    /**
     * 用户取消点赞
     * @return
     */
    @ApiOperation("用户取消点赞")
    @PostMapping("/hotel/comment/unlike/{commentId}")
    public int usercommentunlike(@PathVariable("commentId")Long commentId) {
        return hotelCommentService.updateCommentMiusZannum(commentId);
    }



//    public void list(){
//        TravelUser TravelUser = UserThrealLocal.get();
//
//        comments.stream().peek(comment->{
//            Long commentId = comment.getId();
//            String countStr = redisOperator.getHashValue("redis:comment:like:count",commentId);
//            comment.setZansCount(countStr);
//            // 查询用户是否点赞
//            String hget = redisOperator.hget("redis:comment:like:user", TravelUser.getId() + ":" + commentId);
//            if(Vsserts.isNotEmpty(hget)){
//                comment.setIsLike(true);
//            }
//        })
//    }

}
