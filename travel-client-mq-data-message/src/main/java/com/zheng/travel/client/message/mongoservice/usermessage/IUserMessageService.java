package com.zheng.travel.client.message.mongoservice.usermessage;

import com.zheng.travel.client.message.mongo.UserMessageMo;

public interface IUserMessageService {
    /**
     * 保存消息
     * @param userMessageMo
     * @return
     */
    UserMessageMo saveMessage(UserMessageMo userMessageMo);

}
