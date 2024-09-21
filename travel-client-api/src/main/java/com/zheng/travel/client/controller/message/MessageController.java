package com.zheng.travel.client.controller.message;

import com.zheng.travel.client.bo.OrderMessageBo;
import com.zheng.travel.client.mongoservice.ordermessage.IOrderMessageService;
import com.zheng.travel.client.mongoservice.sysmessage.ISysMessageService;
import com.zheng.travel.client.mongoservice.usermessage.IUserMessageService;
import com.zheng.travel.client.service.usermessage.IUserMesageStatusService;
import com.zheng.travel.client.utils.fn.asserts.Vsserts;
import com.zheng.travel.client.controller.APIBaseController;
import com.zheng.travel.client.entity.TravelUser;
import com.zheng.travel.client.mongo.SysMessageMo;
import com.zheng.travel.client.mongo.UserMessageMo;
import com.zheng.travel.client.threadlocal.UserThrealLocal;
import com.zheng.travel.client.vo.OrderMessageVo;
import com.zheng.travel.client.vo.SysMessageVo;
import com.zheng.travel.client.vo.UserMessageVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "消息模块")
@RestController
@RequiredArgsConstructor
public class MessageController extends APIBaseController {

    // 订单消息sevice对象
    private final IOrderMessageService orderMessageService;
    // 用户消息sevice对象
    private final IUserMessageService userMessageService;
    // 系统消息sevice对象(系统消息是在后台添加)
    private final ISysMessageService sysMessageService;
    // 系统消息sevice对象(系统消息是在后台添加)
    private final IUserMesageStatusService userMesageStatusService;


    /**
     * 查询用户的分页信息
     *
     * @param orderMessageVo
     * @return
     */
    @ApiOperation("查询消息分页 - 订单消息")
    @PostMapping("/message/order/page")
    public Page<OrderMessageBo> queryOrderMessagePage(@RequestBody OrderMessageVo orderMessageVo) {
        TravelUser TravelUser = UserThrealLocal.get();
        orderMessageVo.setUserId(TravelUser.getId() + "");
        // 1: 根据用户id查询对应的订单消息
        Page<OrderMessageBo> messagePage = orderMessageService.findMessagePage(orderMessageVo);
        // 如果用户一条都没有读过，那就直接返回
        if (Vsserts.isNotNull(messagePage)) {
            // 2 :我要把我读的消息的状态同步到我的订单消息里面去
            List<OrderMessageBo> messageList = messagePage.getContent();
            if (!CollectionUtils.isEmpty(messageList)) {
                messageList = messageList.stream().peek(message -> {
                    String messageId = message.getId();
                    String filed = messageId + "_" + TravelUser.getId();
                    boolean flag = redisOperator.hasKey("kss_user_message_order", filed);
                    message.setChecked(flag ? 1 : 0);
                }).collect(Collectors.toList());
            }
        }
        return messagePage;
    }

//    /**
//     * 查询用户的分页信息 ----DB
//     *
//     * @param orderMessageVo
//     * @return
//     */
//    @ApiOperation("查询消息分页 - 订单消息")
//    @PostMapping("/message/order/page")
//    public Page<OrderMessageBo> queryOrderMessagePage(@RequestBody OrderMessageVo orderMessageVo) {
//        TravelUser TravelUser = UserThrealLocal.get();
//        orderMessageVo.setUserId(TravelUser.getId() + "");
//        // 1: 根据用户id查询对应的订单消息
//        Page<OrderMessageBo> messagePage = orderMessageService.findMessagePage(orderMessageVo);
//
//        // 2: 读取我的读过的消息列表 -- 存储每个而用户各自信息的状态
//        LambdaQueryWrapper<UserMesageStatus> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//        lambdaQueryWrapper.eq(UserMesageStatus::getUserId, TravelUser.getId());
//        List<UserMesageStatus> userMesageStatusList = userMesageStatusService.list(lambdaQueryWrapper);
//
//        // 如果用户一条都没有读过，那就直接返回
//        if (Vsserts.isNotNull(messagePage) && !CollectionUtils.isEmpty(userMesageStatusList)) {
//            // 2 :我要把我读的消息的状态同步到我的订单消息里面去
//            List<OrderMessageBo> messageList = messagePage.getContent();
//            if (!CollectionUtils.isEmpty(messageList)) {
//                messageList =  messageList.stream().peek(message -> {
//                    String messageId = message.getId();
//                    // 拿用户消息每一条消息列表到用户已读的列表中进行过滤匹配，如果匹配到就返回个数
//                    Long count = userMesageStatusList.stream()
//                            .filter(usermessage -> usermessage.getMessageId().equals(messageId)).count();
//                    message.setChecked(count.intValue());
//                }).collect(Collectors.toList());
//            }
//        }
//
//        return messagePage;
//    }

    /**
     * 查询用户的分页信息
     *
     * @param userMessageVo
     * @return
     */
    @ApiOperation("查询消息分页 - 用户消息")
    @PostMapping("/message/user/page")
    public Page<UserMessageMo> queryOrderMessagePage(@RequestBody UserMessageVo userMessageVo) {
        TravelUser TravelUser = UserThrealLocal.get();
        userMessageVo.setUserId(TravelUser.getId() + "");
        return userMessageService.findMessagePage(userMessageVo);
    }

    /**
     * 查询用户的分页信息
     *
     * @param sysMessageVo
     * @return
     */
    @ApiOperation("查询消息分页 - 系统消息")
    @PostMapping("/message/sys/page")
    public Page<SysMessageMo> queryOrderMessagePage(@RequestBody SysMessageVo sysMessageVo) {
        TravelUser TravelUser = UserThrealLocal.get();
        sysMessageVo.setUserId(TravelUser.getId() + "");
        return sysMessageService.findMessagePage(sysMessageVo);
    }

}
