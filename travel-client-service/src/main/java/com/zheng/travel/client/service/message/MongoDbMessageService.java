package com.zheng.travel.client.service.message;

import com.zheng.travel.client.bo.TravelUserBo;
import com.zheng.travel.client.event.MongoEvent;
import com.zheng.travel.client.utils.date.TmDateUtil;
import com.zheng.travel.client.entity.UserOrder;
import com.zheng.travel.client.mongo.OrderMessageMo;
import com.zheng.travel.client.mongo.UserMessageMo;
import com.zheng.travel.client.mongoservice.ordermessage.IOrderMessageService;
import com.zheng.travel.client.mongoservice.usermessage.IUserMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MongoDbMessageService {

    @Autowired
    private IOrderMessageService orderMessageService;
    @Autowired
    private IUserMessageService userMessageService;

    /**
     * 下单消息
     */
    @Async
    public void saveMongoMessage(MongoEvent mongoEvent) {
        // 获取方法的返回值
        Object methodResult = mongoEvent.getMethodResult();
        // 如果你方法是无返回值 void，就不处理了
        if (null == methodResult) {
            return;
        }

        if (methodResult instanceof TravelUserBo) {
            TravelUserBo travelUserBo = (TravelUserBo) methodResult;
            // 发送消息
            UserMessageMo userMessageMo = new UserMessageMo();
            userMessageMo.setTitle("注册通知");
            userMessageMo.setMsgcontent("恭喜你【" + travelUserBo.getNickname() + "】加入到旅游平台，奖励积分50。");
            userMessageMo.setMsgtype(1);
            userMessageMo.setOpid(travelUserBo.getId());
            userMessageMo.setCreateTime(TmDateUtil.getCurrentTime());
            userMessageMo.setNickname(travelUserBo.getNickname());
            userMessageMo.setUserid(travelUserBo.getId() + "");
            userMessageMo.setAvatar(travelUserBo.getAvatar());
            userMessageMo.setIcon("/static/icon-item-001.png");
            userMessageService.saveMessage(userMessageMo);
        } else if (methodResult instanceof UserOrder) {
            UserOrder order = (UserOrder) methodResult;
            // 发送消息
            OrderMessageMo orderMessageMo = new OrderMessageMo();
            orderMessageMo.setTitle("订单通知");
            orderMessageMo.setMsgcontent("用户【" + order.getNickname() + "】您好，" +
                    "你已经成功入住【" + order.getHotelTitle() + "_" + order.getHotelDetailTitle() + "】 " +
                    "\n 酒店的地址:" + order.getHotelAddress() + " \n联系方式是：" + order.getHotelTel() + "。");
            orderMessageMo.setMsgtype(2);
            orderMessageMo.setOpid(order.getId());
            orderMessageMo.setCreateTime(TmDateUtil.getCurrentTime());
            orderMessageMo.setNickname(order.getNickname());
            orderMessageMo.setUserid(order.getUserId() + "");
            orderMessageMo.setAvatar(order.getAvatar());
            orderMessageMo.setIcon("/static/icon-item-002.png");
            orderMessageService.saveMessage(orderMessageMo);
        }

    }

}
