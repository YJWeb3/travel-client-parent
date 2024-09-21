package com.zheng.travel.client.controller.pay.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zheng.travel.client.controller.pay.relation.TravelStaticParameter;
import com.zheng.travel.client.idwork.SnowflakeIdWorker;
import com.zheng.travel.client.result.ex.TravelBusinessException;
import com.zheng.travel.client.service.hotel.IHotelService;
import com.zheng.travel.client.service.hotel.category.IHotelTypeService;
import com.zheng.travel.client.service.hoteltypemiddle.IHotelTypeMiddleService;
import com.zheng.travel.client.service.userorder.IUserOrderService;
import com.zheng.travel.client.utils.JsonUtil;
import com.zheng.travel.client.utils.date.TmDateUtil;
import com.zheng.travel.client.utils.fn.asserts.Vsserts;
import com.zheng.travel.client.controller.pay.relation.HttpUtils;
import com.zheng.travel.client.controller.pay.relation.WeixinchatPayUtils;
import com.zheng.travel.client.entity.Hotel;
import com.zheng.travel.client.entity.HotelTypeMiddle;
import com.zheng.travel.client.entity.TravelUser;
import com.zheng.travel.client.entity.UserOrder;
import com.zheng.travel.client.threadlocal.UserThrealLocal;
import com.zheng.travel.client.vo.OrderPayVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class WeixinSmallPayService implements IPayService {


    @Autowired
    private IHotelTypeService hotelTypeService;
    @Autowired
    private IHotelTypeMiddleService hotelTypeMiddleService;
    @Autowired
    private IHotelService hotelService;
    @Autowired
    private IUserOrderService userOrderService;


    @Override
    public Map<String, Object> payment(OrderPayVo orderPayVo) {


        if (Vsserts.isEmpty(orderPayVo.getHotelDetailId())) {
            throw new TravelBusinessException(701, "查无此房型数据...");
        }

        if (Vsserts.isEmpty(orderPayVo.getOpenid())) {
            throw new TravelBusinessException(701, "支付用户找不到...");
        }

        UserOrder userOrderDeatilByOrderNo = null;
        if (orderPayVo != null && Vsserts.isNotEmpty(orderPayVo.getOrderno())) {
            userOrderDeatilByOrderNo = userOrderService.getUserOrderDeatilByOrderNo(orderPayVo.getOrderno());
            if (Vsserts.isNotNull(userOrderDeatilByOrderNo) && userOrderDeatilByOrderNo.getStatus() != 0) {
                if (userOrderDeatilByOrderNo.getStatus().equals(1)) {
                    throw new TravelBusinessException(701, "订单已支付了...");
                } else if (userOrderDeatilByOrderNo.getStatus().equals(2)) {
                    throw new TravelBusinessException(701, "订单已完成了...");
                } else if (userOrderDeatilByOrderNo.getStatus().equals(-1)) {
                    throw new TravelBusinessException(701, "订单已取消了...");
                }
            }
        }

        /*************************************支付的用户*********************************/
        // 1： 支付的用户
        TravelUser user = UserThrealLocal.get();
        log.info("支付的产品是:{}，用户为：{}", user.getId());

        /*************************************业务数据*********************************/
        // 2: 根据酒店的房型ID查询房型的信息
        HotelTypeMiddle hotelTypeMiddle = hotelTypeMiddleService.getById(Long.parseLong(orderPayVo.getHotelDetailId()));
        if (Vsserts.isNull(hotelTypeMiddle)) {
            throw new TravelBusinessException(701, "查无此房型数据...");
        }

        if (hotelTypeMiddle.getStorenum() != null && hotelTypeMiddle.getStorenum() <= 0) {
            throw new TravelBusinessException(701, "库存不足...");
        }

        // 3: 根据酒店ID查询酒店信息
        Hotel hotel = hotelService.getById(hotelTypeMiddle.getHotelId());


        // 计算用户入住的天数
        int days = TmDateUtil.differentDays(orderPayVo.getEndDate() + " 23:59:59", orderPayVo.getStartDate() + " 00:00:00");
        if (days <= 0) {
            throw new TravelBusinessException(701, "入住的天数不正确...");
        }

        // 4: 支付产品金额
        String money = String.valueOf(hotelTypeMiddle.getRealprice().doubleValue() * days);
        // 4: 支付产品的标题
        String title = hotel.getTitle() + "_" + hotelTypeMiddle.getTitle();
        if (Vsserts.isNotEmpty(title) && title.length() > 100) {
            title = title.substring(0, 100);
        }


        /*************************************微信支付的参数 -start*********************************/
        //封装请求参数
        Map<String, Object> map = new HashMap();
        // 支付的产品（小程序或者公众号，主要需要和微信支付绑定哦）
        map.put("appid", TravelStaticParameter.appId);
        // 支付的商户号
        map.put("mchid", TravelStaticParameter.mchId);

        // 用雪花算法生成一个唯一的订单ID
        SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker();
        String out_trade_no = String.valueOf(snowflakeIdWorker.nextId());
        //临时写死配置
        map.put("description", title);
        map.put("out_trade_no", out_trade_no);
        map.put("notify_url", TravelStaticParameter.notifyUrl);

        Map<String, Object> amount = new HashMap();
        //订单金额 单位分 float double decimal varchar
        amount.put("total", Integer.parseInt(getMoney(money)));
        amount.put("currency", "CNY");
        map.put("amount", amount);
        // 设置小程序所需的openid
        Map<String, Object> payermap = new HashMap();
        payermap.put("openid", orderPayVo.getOpenid());
        map.put("payer", payermap);

        // 附件参数
        Map<String, Object> attachMap = new HashMap<>();
        attachMap.put("userId", user.getId());
        attachMap.put("hotelId", hotel.getId());
        attachMap.put("hotelDetaiId", hotelTypeMiddle.getId());
        attachMap.put("money", money);
        map.put("attach", JsonUtil.obj2String(attachMap));
        /*************************************微信支付的参数 -end*********************************/


        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String body = objectMapper.writeValueAsString(map);
            // 1: 把上面map参数和请求地址和请求方式通过RSA加密，然后发起httpclient的post
            Map<String, Object> stringObjectMap = HttpUtils.doPostWexin(TravelStaticParameter.unifiedOrderUrl, body);
            // 2：把微信支付服务器返回的结果进行组装返回即可
            HashMap<String, Object> dataMap = WeixinchatPayUtils.getTokenWeixin(TravelStaticParameter.appId,
                    String.valueOf(stringObjectMap.get("prepay_id")));
            HashMap<String, Object> resultMap = new HashMap<>();
            // 创建一个订单
            hotelTypeMiddle.setRealprice(new BigDecimal(money));


            // 如果userOrderDeatilByOrderNo==null代表没有orderno，说明就创建订单,否则说明有订单，就不用创建，直接返回
            if (userOrderDeatilByOrderNo == null) {
                userOrderDeatilByOrderNo = userOrderService.createUserOrder(hotel, hotelTypeMiddle, user, orderPayVo, out_trade_no, days);
            }

            resultMap.put("dataMap", dataMap);// dataMap 获取到prepay_id
            resultMap.put("paramMap", map);//组装前台完整的微信支付的参数
            resultMap.put("userorder", userOrderDeatilByOrderNo);

            return resultMap;
        } catch (Exception ex) {
            log.info("下单出现异常,{}", ex.getMessage());
            throw new TravelBusinessException(701, "下单出现异常");
        }
    }


    /**
     * 支付成功修改订单状态
     *
     * @param orderPayVo
     */
    @Override
    public int paymentSuccess(OrderPayVo orderPayVo) {
        UserOrder userOrder = null;
        // 如果当前已经存在一笔订单，就不做任何处理，直接继续支付即可
        if (Vsserts.isNotEmpty(orderPayVo.getOrderno())) {
            userOrder = userOrderService.getUserOrderDeatilByOrderNo(orderPayVo.getOrderno());
        }

        if (Vsserts.isNotNull(userOrder)) {
            UserOrder newUserOrder = new UserOrder();
            newUserOrder.setId(userOrder.getId());
            newUserOrder.setStatus(1);
            userOrderService.updateById(newUserOrder);
            return 1;
        }

        return 0;
    }

}
