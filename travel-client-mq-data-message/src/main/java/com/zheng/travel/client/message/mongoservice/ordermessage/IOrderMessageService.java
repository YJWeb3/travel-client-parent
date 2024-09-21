package com.zheng.travel.client.message.mongoservice.ordermessage;

import com.zheng.travel.client.message.mongo.OrderMessageMo;

public interface IOrderMessageService {
    /**
     * 保存消息
     * @param orderMessageMo
     * @return
     */
    OrderMessageMo saveMessage(OrderMessageMo orderMessageMo);

}
