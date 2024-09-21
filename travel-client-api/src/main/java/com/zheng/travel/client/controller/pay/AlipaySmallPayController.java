package com.zheng.travel.client.controller.pay;

import com.zheng.travel.client.controller.APIBaseController;
import com.zheng.travel.client.controller.pay.service.IPayService;
import com.zheng.travel.client.vo.OrderPayVo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Log4j2
public class AlipaySmallPayController extends APIBaseController {

    @Autowired
    @Qualifier("alipayPayService")
    private IPayService payService;

    /**
     * 付款订单Api,根据传入的订单号 生成付款二维码
     */
    @RequestMapping("/alipay")
    public Map<String, Object> jsapiweixinpay(@RequestBody OrderPayVo orderPayVo) {
        return payService.payment(orderPayVo);
    }


    /**
     * 支付成功进入回调地址
     */
    @PostMapping("/alipay/success")
    public int jsapiweixinpaysuccess(@RequestBody OrderPayVo orderPayVo) {
        return payService.paymentSuccess(orderPayVo);
    }

}