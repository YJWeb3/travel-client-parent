package com.zheng.travel.client.service.userorder;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zheng.travel.client.bo.UserOrderBo;
import com.zheng.travel.client.entity.Hotel;
import com.zheng.travel.client.entity.HotelTypeMiddle;
import com.zheng.travel.client.entity.TravelUser;
import com.zheng.travel.client.entity.UserOrder;
import com.zheng.travel.client.vo.OrderPayVo;
import com.zheng.travel.client.vo.UserOrderVo;
import com.zheng.travel.client.vo.UserVo;

import java.util.List;
import java.util.Map;

public interface IUserOrderService extends IService<UserOrder> {

    /***
     * @param  userOrderVo
     * @return
     */
    Page<UserOrder> findUserOrders(UserOrderVo userOrderVo);


    /**
     * 查询用户订单状态信息
     *
     * @return
     */
    List<Map<String, Object>> selectOrderStatusInfo();

    /**
     * 用户订购酒店下单
     *
     * @param hotel
     * @param hotelTypeMiddle
     * @param user
     * @param orderPayVo
     * @param orderNumber
     * @param days
     * @return
     */
    UserOrder createUserOrder(Hotel hotel,
                              HotelTypeMiddle hotelTypeMiddle,
                              TravelUser user, OrderPayVo orderPayVo, String orderNumber, int days);

    /**
     * 用户下单
     *
     * @param userVo
     * @return
     */
    UserOrderBo createUserOrder(UserVo userVo);


    /**
     * 查询那些超过30分钟未支付的订单
     * 进行失效处理
     */
    void findOrderNoPay();


    /**
     * 根据订单id查询明细信息
     *
     * @param orderId
     * @return
     */
    UserOrder getUserOrderDeatilById(Long orderId);

    /**
     * 查询订单是否存在
     * @param orderNo
     * @return
     */
    UserOrder getUserOrderDeatilByOrderNo(String orderNo);


    /**
     * 用户取消订单
     *
     * @param orderId 订单ID
     * @param userId  用户id
     */
    int cancleOrder(Long orderId, Long userId);


    /**
     * 完成订单
     *
     * @param orderId 订单ID
     * @param userId  用户id
     */
    int finishedOrder(Long orderId, Long userId);


    /**
     * 完成入住
     * @param orderId
     * @param userId
     * @return
     */
    int finishedOrderOver(Long orderId, Long userId);


    /**
     * 删除订单
     *
     * @param orderId 订单ID
     * @param userId  用户id
     */
    int removeOrder(Long orderId, Long userId);


    /**
     * 评论订单
     * @param orderId 订单ID
     * @param userId  用户id
     * @param comment 评价信息
     */
    int commentOrder(Long orderId, Long userId, String comment);

}
