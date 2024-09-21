package com.zheng.travel.client.service.message;

import com.zheng.travel.client.utils.date.TmDateUtil;
import com.zheng.travel.client.entity.UserOrder;
import com.zheng.travel.client.mongo.MessageMo;
import com.zheng.travel.client.service.weixin.WeixinData;
import com.zheng.travel.client.vo.TravelUserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EsDbMessageService {


    public MessageMo getMessageMo(Object[] parameters) {
        Object parameter = null;
        if (parameters != null && parameters.length > 0) {
            parameter = parameters[0];
        }

        if (null == parameter) {
            return null;
        }

        if (parameter instanceof TravelUserVo) {
            TravelUserVo TravelUserVo = (TravelUserVo) parameter;
            // 发送消息
            MessageMo messageMo = new MessageMo();
            messageMo.setTitle("注册通知");
            messageMo.setMsgcontent("恭喜你【" + TravelUserVo.getNickname() + "】加入到旅游平台，奖励积分50。");
            messageMo.setMsgtype(1);
            messageMo.setOpid(Long.parseLong(TravelUserVo.getId()));
            messageMo.setCreateTime(TmDateUtil.getCurrentTime());
            messageMo.setNickname(TravelUserVo.getNickname());
            messageMo.setUserid(TravelUserVo.getId() + "");
            messageMo.setAvatar(TravelUserVo.getAvatar());
            messageMo.setIcon("/static/icon-item-001.png");
            return messageMo;
        } else if (parameter instanceof WeixinData) {
            WeixinData weixinData = (WeixinData) parameter;
            // 发送消息
            MessageMo messageMo = new MessageMo();
            messageMo.setTitle("注册通知");
            messageMo.setMsgcontent("恭喜你【" + weixinData.getNickName() + "】加入到旅游平台，奖励积分50。");
            messageMo.setMsgtype(1);
            messageMo.setOpid(weixinData.getUserid());
            messageMo.setCreateTime(TmDateUtil.getCurrentTime());
            messageMo.setNickname(weixinData.getNickName());
            messageMo.setUserid(weixinData.getUserid() + "");
            messageMo.setAvatar(weixinData.getAvatarUrl());
            messageMo.setIcon("/static/icon-item-001.png");
            return messageMo;
        } else if (parameter instanceof UserOrder) {
            UserOrder order = (UserOrder) parameter;
            // 发送消息
            MessageMo messageMo = new MessageMo();
            messageMo.setTitle("订单通知");
            messageMo.setMsgcontent("用户【" + order.getNickname() + "】您好，" +
                    "你已经成功入住【"+order.getHotelTitle()+"_"+order.getHotelDetailTitle()+"】 \n 酒店的地址:"+order.getHotelAddress()+" \n联系方式是："+order.getHotelTel()+"。");
            messageMo.setMsgtype(2);
            messageMo.setOpid(order.getId());
            messageMo.setCreateTime(TmDateUtil.getCurrentTime());
            messageMo.setCreateTime(TmDateUtil.getCurrentTime());
            messageMo.setNickname(order.getNickname());
            messageMo.setUserid(order.getUserId() + "");
            messageMo.setAvatar(order.getAvatar());
            messageMo.setIcon("/static/icon-item-002.png");
            return messageMo;
        }

        return null;
    }


    /**
     * 下单消息
     */
    @Async
    public void saveESMessage(Object[] parameters) {
        // 发送消息
        MessageMo messageMo = getMessageMo(parameters);
        //
        log.info("es save {}", messageMo);

    }

}
