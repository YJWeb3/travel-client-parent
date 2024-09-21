package com.zheng.travel.client.message.mongoservice.message;

import com.zheng.travel.client.message.mongo.MessageMo;


public interface IMessageService {

    /**
     * 保存消息
     * @param messageMo
     * @return
     */
    MessageMo saveMessage(MessageMo messageMo);


}
