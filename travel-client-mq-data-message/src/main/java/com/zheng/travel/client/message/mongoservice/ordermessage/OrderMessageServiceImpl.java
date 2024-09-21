package com.zheng.travel.client.message.mongoservice.ordermessage;

import com.zheng.travel.client.message.mongo.OrderMessageMo;
import com.zheng.travel.client.message.mongorepository.OrderMessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderMessageServiceImpl implements IOrderMessageService {

    @Autowired
    private OrderMessageRepository orderMessageRepository;

    @Override
    public OrderMessageMo saveMessage(OrderMessageMo orderMessageMo) {
        orderMessageMo.setId(null);
        OrderMessageMo orderMessageMo1 = orderMessageRepository.save(orderMessageMo);
        return orderMessageMo1;
    }


}
