package com.zheng.travel.client.mongoservice.ordermessage;

import com.zheng.travel.client.bo.OrderMessageBo;
import com.zheng.travel.client.mongo.OrderMessageMo;
import com.zheng.travel.client.mongorepository.OrderMessageRepository;
import com.zheng.travel.client.vo.OrderMessageVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderMessageServiceImpl implements IOrderMessageService {

    @Autowired
    private OrderMessageRepository orderMessageRepository;

    @Override
    public OrderMessageMo saveMessage(OrderMessageMo orderMessageMo) {
        OrderMessageMo orderMessageMo1 = orderMessageRepository.save(orderMessageMo);
        return orderMessageMo1;
    }


    @Override
    public Page<OrderMessageBo> findMessagePage(OrderMessageVo orderMessageVo) {
        if (orderMessageVo.getPageNo() <= 0) {
            orderMessageVo.setPageNo(0);
        }
        if (orderMessageVo.getPageNo() > 0) {
            orderMessageVo.setPageNo(orderMessageVo.getPageNo() - 1);
        }
        // 1: 设置分页和排序 这个是mongodb的属性名
        Sort sort = Sort.by(Sort.Direction.DESC, "ctime");
        Pageable pageable = PageRequest.of(orderMessageVo.getPageNo(), orderMessageVo.getPageSize(), sort);

        //2:查询返条件设置
        OrderMessageMo messageMo = new OrderMessageMo();
        messageMo.setUserid(orderMessageVo.getUserId());
        // 4: 设置查询条件和规则
        Example example = Example.of(messageMo);
        // 5：查询分页返回
        Page<OrderMessageMo> messageMoPage = orderMessageRepository.findAll(example, pageable);
        // 6:分页返回
        return this.tranferPageMongoBo(messageMoPage,OrderMessageBo.class);
    }

}
