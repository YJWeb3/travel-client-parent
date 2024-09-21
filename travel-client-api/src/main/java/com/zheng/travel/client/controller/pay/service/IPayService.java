package com.zheng.travel.client.controller.pay.service;

import com.zheng.travel.client.vo.OrderPayVo;

import java.util.Map;

public interface IPayService {


    /**
     * 支付方法
     * @param orderPayVo
     */
    Map<String,Object> payment(OrderPayVo orderPayVo);


    /**
     * 支付成功修改订单状态
     * @param orderPayVo
     */
    int paymentSuccess(OrderPayVo orderPayVo);


    /**
     * 元转换成分
     *
     * @param money
     * @return
     */
    default String getMoney(String money) {
        if (money == null || money.equalsIgnoreCase("0")) {
            return "";
        }
        // 金额转化为分为单位
        // 处理包含, ￥ 或者$的金额
        String currency = money.replaceAll("\\$|\\￥|\\,", "");
        int index = currency.indexOf(".");
        int length = currency.length();
        Long amLong = 0L;
        if (index == -1) {
            amLong = Long.valueOf(currency + "00");
        } else if (length - index >= 3) {
            amLong = Long.valueOf((currency.substring(0, index + 3)).replace(".", ""));
        } else if (length - index == 2) {
            amLong = Long.valueOf((currency.substring(0, index + 2)).replace(".", "") + 0);
        } else {
            amLong = Long.valueOf((currency.substring(0, index + 1)).replace(".", "") + "00");
        }
        return amLong.toString();
    }

}
