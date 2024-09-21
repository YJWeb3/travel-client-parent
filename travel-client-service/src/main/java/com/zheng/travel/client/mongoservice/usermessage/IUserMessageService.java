package com.zheng.travel.client.mongoservice.usermessage;

import com.zheng.travel.client.mongo.UserMessageMo;
import com.zheng.travel.client.vo.UserMessageVo;
import org.springframework.data.domain.Page;

public interface IUserMessageService {
    /**
     * 保存消息
     * @param userMessageMo
     * @return
     */
    UserMessageMo saveMessage(UserMessageMo userMessageMo);


    /**
     * 分页查询
     * @return
     */
    Page<UserMessageMo> findMessagePage(UserMessageVo userMessageVo);
}
