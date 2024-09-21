package com.zheng.travel.client.mongoservice.ordermessage;

import com.zheng.travel.client.bo.OrderMessageBo;
import com.zheng.travel.client.service.MongoBaseService;
import com.zheng.travel.client.mongo.OrderMessageMo;
import com.zheng.travel.client.vo.OrderMessageVo;
import org.springframework.data.domain.Page;

public interface IOrderMessageService extends MongoBaseService {
    /**
     * 保存消息
     * @param orderMessageMo
     * @return
     */
    OrderMessageMo saveMessage(OrderMessageMo orderMessageMo);


    /**
     * 分页查询
     * @return
     */
    Page<OrderMessageBo> findMessagePage(OrderMessageVo orderMessageVo);
}
