package com.zheng.travel.client.controller.userorder;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zheng.travel.client.controller.APIBaseController;
import com.zheng.travel.client.service.userorder.IUserOrderService;
import com.zheng.travel.client.entity.TravelUser;
import com.zheng.travel.client.entity.UserOrder;
import com.zheng.travel.client.threadlocal.UserThrealLocal;
import com.zheng.travel.client.vo.UserOrderCommentVo;
import com.zheng.travel.client.vo.UserOrderVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@Api(tags = "用户订单")
@Slf4j
public class UserOrderController extends APIBaseController {

    @Autowired
    private IUserOrderService userOrderService;

    /***
     * @param  userOrderVo
     * @return
     */
    @PostMapping("/user/order/load")
    @ApiOperation("用户订单查询")
    public Page<UserOrder> findUserOrders(@RequestBody UserOrderVo userOrderVo) {
        return userOrderService.findUserOrders(userOrderVo);
    }

    /***
     * @return
     */
    @PostMapping("/user/status/load")
    @ApiOperation("用户订单状态查询")
    public List<Map<String, Object>> selectOrderStatusInfo() {
        return userOrderService.selectOrderStatusInfo();
    }


    /**
     * 根据订单id查询明细信息
     *
     * @param orderId
     * @return
     */
    @PostMapping("/userorder/get/{orderId}")
    public UserOrder getUserOrderDeatilById(@PathVariable("orderId") Long orderId) {
        return userOrderService.getUserOrderDeatilById(orderId);
    }


    /**
     * 用户取消订单
     *
     * @param orderId 订单ID
     */
    @PostMapping("/userorder/cancle/{orderId}")
    public int cancleOrder(@PathVariable("orderId") Long orderId) {
        TravelUser TravelUser = UserThrealLocal.get();
        return userOrderService.cancleOrder(orderId, TravelUser.getId());
    }


    /**
     * 完成订单
     *
     * @param orderId 订单ID
     */
    @PostMapping("/userorder/finished/{orderId}")
    public int finishedOrder(@PathVariable("orderId") Long orderId) {
        TravelUser TravelUser = UserThrealLocal.get();
        return userOrderService.finishedOrder(orderId, TravelUser.getId());
    }

    /**
     * 完成订单
     *
     * @param orderId 订单ID
     */
    @PostMapping("/userorder/finishedover/{orderId}")
    public int finishedOrderOver(@PathVariable("orderId") Long orderId) {
        TravelUser TravelUser = UserThrealLocal.get();
        return userOrderService.finishedOrderOver(orderId, TravelUser.getId());
    }


    /**
     * 删除订单
     *
     * @param orderId 订单ID
     */
    @PostMapping("/userorder/remove/{orderId}")
    public int removeOrder(@PathVariable("orderId") Long orderId) {
        TravelUser TravelUser = UserThrealLocal.get();
        return userOrderService.removeOrder(orderId, TravelUser.getId());
    }


    /**
     * 评论订单
     *
     * @param userOrderCommentVo
     */
    @PostMapping("/userorder/comment")
    public int commentOrder(@RequestBody UserOrderCommentVo userOrderCommentVo) {
        TravelUser TravelUser = UserThrealLocal.get();
        return userOrderService.commentOrder(userOrderCommentVo.getOrderId(), TravelUser.getId(), userOrderCommentVo.getComment());
    }
}
