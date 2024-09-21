package com.zheng.travel.client.mongoservice.message;

import com.zheng.travel.client.mongo.MessageMo;
import com.zheng.travel.client.vo.MessageVo;
import org.springframework.data.domain.Page;

public interface IMessageService {

    /**
     * 保存消息
     * @param messageMo
     * @return
     */
    MessageMo saveMessage(MessageMo messageMo);


    /**
     * 分页查询
     * @return
     */
    Page<MessageMo> findMessagePage(MessageVo messageVo);
}
