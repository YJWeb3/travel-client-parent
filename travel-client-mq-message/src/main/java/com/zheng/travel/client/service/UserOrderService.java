package com.zheng.travel.client.service;

import com.zheng.travel.client.dao.UserOrderDao;
import com.zheng.travel.client.dto.UserOrderDto;
import com.zheng.travel.client.entity.UserOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserOrderService  implements  IUserOrderService{

    @Autowired
    private UserOrderDao userOrderDao;

    /**
     * 更新订单的状态为 -1
     * status = -1 是失效的状态
     * status = 0 是未支付
     * status = 1 是已支付
     * status = 2 是已完成
     *
     * @param userOrderDto
     * @return
     */
    @Override
    public boolean updateUserOrderStatus(UserOrderDto userOrderDto) {
        //第一步，通过Repository对象把实体根据ID查询出来
        //第二部，往查出来的实体对象进行set各个字段
        //第三步，通过Repository接口的save方法进行保存
        UserOrder userOrder = userOrderDao.getById(userOrderDto.getId());
        if (null != userOrder && userOrder.getStatus().equals(0)) {
            userOrder.setId(userOrderDto.getId());
            userOrder.setStatus(-1);
            UserOrder save = userOrderDao.save(userOrder);
            return save != null;
        } else {
            return false;
        }
    }
}
