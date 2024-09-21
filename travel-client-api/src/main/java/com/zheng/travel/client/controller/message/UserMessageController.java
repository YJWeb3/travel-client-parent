package com.zheng.travel.client.controller.message;

import com.zheng.travel.client.service.usermessage.IUserMesageStatusService;
import com.zheng.travel.client.controller.APIBaseController;
import com.zheng.travel.client.entity.TravelUser;
import com.zheng.travel.client.threadlocal.UserThrealLocal;
import com.zheng.travel.client.vo.UserMesageStatusVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "用户消息状态管理")
@RestController
@RequiredArgsConstructor
public class UserMessageController extends APIBaseController {

    // 订单消息sevice对象
    private final IUserMesageStatusService userMesageStatusService;

//    /**
//     * 查询用户的分页信息---db
//     *
//     * @param userMesageStatusVo
//     * @return
//     */
//    @ApiOperation("保存用户消息的状态")
//    @PostMapping("/message/userstatus/save")
//    public boolean queryOrderMessagePage(@RequestBody UserMesageStatusVo userMesageStatusVo) {
//        TravelUser TravelUser = UserThrealLocal.get();
//        LambdaQueryWrapper<UserMesageStatus> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//        lambdaQueryWrapper.eq(UserMesageStatus::getUserId, TravelUser.getId());
//        lambdaQueryWrapper.eq(UserMesageStatus::getMessageId, userMesageStatusVo.getMessageId());
//        // 判断当前用户是否以及读过了
//        long count = userMesageStatusService.count(lambdaQueryWrapper);
//        // 如果没有未读就直接保存读状态
//        if (count == 0) {
//            // 保存用户读状态
//            UserMesageStatus userMesageStatus = new UserMesageStatus();
//            userMesageStatus.setUserId(TravelUser.getId());
//            userMesageStatus.setMessageId(userMesageStatusVo.getMessageId());
//            return userMesageStatusService.saveOrUpdate(userMesageStatus);
//        }
//        // 如果已读，直接返回true
//        return true;
//    }


    /**
     * 查询用户的分页信息
     *
     * @param userMesageStatusVo
     * @return
     */
    @ApiOperation("保存用户消息的状态")
    @PostMapping("/message/userstatus/save")
    public boolean queryOrderMessagePage(@RequestBody UserMesageStatusVo userMesageStatusVo) {
       try {
           TravelUser TravelUser = UserThrealLocal.get();
           String tablenameKey = "kss_user_message_order";
           String field = String.valueOf(userMesageStatusVo.getMessageId() + "_" + TravelUser.getId());
           boolean hasKey = redisOperator.hasKey(tablenameKey, field);
           if (!hasKey) {
               redisOperator.hset(tablenameKey, field, "1");
               return true;
           }
           // 如果已读，直接返回true
           return true;
       }catch (Exception ex){
           ex.printStackTrace();
           return false;
       }
    }

}
