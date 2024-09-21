package com.zheng.travel.client.controller.pay.service;

import com.zheng.travel.client.service.userorder.IUserOrderService;
import com.zheng.travel.client.utils.fn.asserts.Vsserts;
import com.zheng.travel.client.entity.UserOrder;
import com.zheng.travel.client.vo.OrderPayVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class AlipayPayService implements IPayService {

    @Autowired
    private IUserOrderService userOrderService;


    @Override
    public Map<String, Object> payment(OrderPayVo orderPayVo) {
        return null;
    }






    /**
     * 支付成功修改订单状态
     * @param orderPayVo
     */
    @Override
    public int paymentSuccess(OrderPayVo orderPayVo){
        UserOrder userOrder = null;
        // 如果当前已经存在一笔订单，就不做任何处理，直接继续支付即可
        if (Vsserts.isNotEmpty(orderPayVo.getOrderno())) {
            userOrder = userOrderService.getById(Long.parseLong(orderPayVo.getOrderno()));
        }

        if(Vsserts.isNotNull(userOrder)){
            UserOrder newUserOrder = new UserOrder();
            newUserOrder.setId(userOrder.getId());
            newUserOrder.setStatus(1);
            userOrderService.updateById(newUserOrder);
            return 1;
        }

        return 0;
    }

}
