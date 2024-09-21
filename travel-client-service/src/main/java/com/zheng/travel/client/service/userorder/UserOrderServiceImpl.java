package com.zheng.travel.client.service.userorder;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zheng.travel.client.anno.log.TravelMessage;
import com.zheng.travel.client.bo.UserOrderBo;
import com.zheng.travel.client.entity.*;
import com.zheng.travel.client.event.MongoEvent;
import com.zheng.travel.client.mq.order.UserOrderProducer;
import com.zheng.travel.client.service.BaseService;
import com.zheng.travel.client.service.hotel.comment.IHotelCommentService;
import com.zheng.travel.client.service.hoteltypemiddle.IHotelTypeMiddleService;
import com.zheng.travel.client.utils.fn.asserts.Vsserts;
import com.zheng.travel.client.dto.UserOrderDto;
import com.zheng.travel.client.entity.*;
import com.zheng.travel.client.mapper.userorder.UserOrderMapper;
import com.zheng.travel.client.threadlocal.UserThrealLocal;
import com.zheng.travel.client.vo.OrderPayVo;
import com.zheng.travel.client.vo.UserOrderVo;
import com.zheng.travel.client.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class UserOrderServiceImpl extends ServiceImpl<UserOrderMapper, UserOrder> implements IUserOrderService, BaseService {


    @Autowired
    private UserOrderProducer userOrderProducer;
    @Autowired
    private IHotelTypeMiddleService hotelTypeMiddleService;
    @Autowired
    @Qualifier("hotelCommentServiceImpl")
    private IHotelCommentService hotelCommentService;


    /**
     * 查询用户订单状态信息
     *
     * @return
     */
    @Override
    public List<Map<String, Object>> selectOrderStatusInfo() {
        TravelUser user = UserThrealLocal.get();
        return this.baseMapper.selectOrderStatusInfo(user.getId());
    }


    @Override
    public Page<UserOrder> findUserOrders(UserOrderVo userOrderVo) {
        TravelUser user = UserThrealLocal.get();
        // 1: 设置查询的条件
        Page<UserOrder> page = new Page<>(userOrderVo.getPageNo(), userOrderVo.getPageSize());
        // 2: 设置条件
        LambdaQueryWrapper<UserOrder> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Vsserts.isNotNull(userOrderVo.getPaystatus()),
                UserOrder::getStatus, userOrderVo.getPaystatus());
        // 3: 查询某个用户的订单信息
        lambdaQueryWrapper.eq(UserOrder::getUserId, user.getId());
        // 4:可以根据订单编号查询或者标题查询
        lambdaQueryWrapper.like(Vsserts.isNotNull(userOrderVo.getKeyword()),
                UserOrder::getHotelTitle, userOrderVo.getKeyword());
        // 5: 考虑排序
        lambdaQueryWrapper.orderByDesc(UserOrder::getCreateTime);
        return this.page(page, lambdaQueryWrapper);
    }


    @Override
    @TravelMessage(eventClasses = {MongoEvent.class})
    @Transactional
    public UserOrder createUserOrder(Hotel hotel,
                                     HotelTypeMiddle hotelTypeMiddle,
                                     TravelUser user, OrderPayVo orderPayVo, String orderNumber, int days) {

        UserOrder userOrder = null;
        // 如果当前已经存在一笔订单，就不做任何处理，直接继续支付即可
        if (Vsserts.isNotEmpty(orderPayVo.getOrderno())) {
            userOrder = this.getById(Long.parseLong(orderPayVo.getOrderno()));
        }

        // 如果没有，创建一笔订单
        if (Vsserts.isNull(userOrder)) {
            // 创建一个订单
            userOrder = new UserOrder();
            userOrder.setOrderno(orderNumber + "");
            userOrder.setUserId(user.getId());
            userOrder.setIsdelete(0);
            userOrder.setAvatar(user.getAvatar());
            userOrder.setNickname(user.getNickname());
            userOrder.setPhone(orderPayVo.getPhone());
            userOrder.setStartDate(orderPayVo.getStartDate());
            userOrder.setEndDate(orderPayVo.getEndDate());
            userOrder.setHnum(orderPayVo.getHnum());
            userOrder.setDinfo(orderPayVo.getDinfo());
            userOrder.setStatus(0);//未支付状态
            userOrder.setPaymoney(hotelTypeMiddle.getRealprice().toString());
            userOrder.setHotelId(hotel.getId());
            userOrder.setHotelImg(hotel.getImg());
            userOrder.setHotelAddress(hotel.getAddress());
            userOrder.setHotelTel(hotel.getPhone());
            userOrder.setHotelLgt(hotel.getLgt());
            userOrder.setHotelLan(hotel.getLan());
            userOrder.setHotelTitle(hotel.getTitle());
            userOrder.setHotelDesc(hotel.getDescription());
            userOrder.setHotelDetailId(hotelTypeMiddle.getId());
            userOrder.setHotelDetailTitle(hotelTypeMiddle.getTitle());
            userOrder.setHotelDetailImg(hotelTypeMiddle.getImg());
            userOrder.setTradeno("");
            userOrder.setDays(days);
            userOrder.setOpenid(orderPayVo.getOpenid());
            //userOrder.setIp(IpUtils.getIpAddr(resultMap));
            userOrder.setIpaddress("");
            userOrder.setPaytype(1);
            this.saveOrUpdate(userOrder);

            //1: 订单消息 --- 使用AOP去完成了
            //2: MQ延时订单处理 + AOP
            //UserOrderDto userOrderDto = this.tranferBo(userOrder, UserOrderDto.class);
            //userOrderProducer.pusherOrder(userOrderDto);

            // 扣减库存
            Integer storenum = hotelTypeMiddle.getStorenum();
            if (storenum != null && storenum > 0) {
                HotelTypeMiddle hotelTypeMiddle1 = new HotelTypeMiddle();
                hotelTypeMiddle1.setId(hotelTypeMiddle.getId());
                hotelTypeMiddle1.setStorenum(storenum - 1);
                hotelTypeMiddleService.updateById(hotelTypeMiddle1);
            }
        }

        return userOrder;
    }

    /**
     * 用户下单处理
     *
     * @param userVo
     * @return
     */
    @Override
    @Transactional
    public UserOrderBo createUserOrder(UserVo userVo) {
        // 1: 获取当前下单的用户信息
        TravelUser travelUser = UserThrealLocal.get();
        // 2: 创建用户订单实例
        UserOrder userOrder = new UserOrder();
        userOrder.setOrderno(userVo.getOrderNo());
        userOrder.setStatus(0);// 代表未支付

        userOrder.setUserId(travelUser.getId());
        userOrder.setNickname(travelUser.getNickname());
        userOrder.setAvatar(travelUser.getAvatar());
        log.info("用户开始下单，订单信息是： 【{}】", userOrder);

        // 3：执行数据的订单保存功能
        boolean b = this.saveOrUpdate(userOrder);
        if (b) {
            // 4:获取到订单信息开始进行异步发送处理
            UserOrderBo userOrderBo = tranferBo(userOrder, UserOrderBo.class);
            // 5: 对内进行发送异步处理
            UserOrderDto userOrderDto = tranferBo(userOrder, UserOrderDto.class);
            // 6 : 使用mq进行异步处理
            userOrderProducer.pusherOrder(userOrderDto);
            return userOrderBo;
        } else {
            return null;
        }
    }


    /**
     * sql语句写法：
     * UPDATE travel_user_order SET status = -1 WHERE id in(
     * SELECT c.id from (
     * SELECT id,TIMESTAMPDIFF(MINUTE,create_time,NOW()) as mins FROM travel_user_order WHERE status = 0
     * ) c WHERE c.mins > 30
     * )
     */
    @Override
    public void findOrderNoPay() {

        //这样写也行。
        this.baseMapper.updateNoPayOrderStatus();

//        LambdaQueryWrapper<UserOrder> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//        lambdaQueryWrapper.eq(UserOrder::getStatus,0);
//        List<UserOrder> userOrders = this.list(lambdaQueryWrapper);
//        if(!CollectionUtils.isEmpty(userOrders)) {
//            List<UserOrder> userOrdersFilter = userOrders.parallelStream().filter(userOrder -> {
//                int diffminutes = TmDateUtil.diffminutes(userOrder.getCreateTime(),new Date());
//                return diffminutes >= 20;
//            }).map(userOrder -> {
//                UserOrder userOrder1 = new UserOrder();
//                userOrder1.setId(userOrder.getId());
//                userOrder1.setStatus(-1);
//                return userOrder1;
//            }).collect(Collectors.toList());

//            this.updateBatchById(userOrdersFilter, 100);
//        }
    }


    @Override
    public UserOrder getUserOrderDeatilById(Long orderId) {
        return this.getById(orderId);
    }

    /**
     * 查询订单是否存在
     * @param orderNo
     * @return
     */
    @Override
    public UserOrder getUserOrderDeatilByOrderNo(String orderNo){
        LambdaQueryWrapper<UserOrder> lambdaQueryWrapper
                = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserOrder::getOrderno,orderNo);
        return this.getOne(lambdaQueryWrapper);
    }


    /**
     * 用户取消订单
     *
     * @param orderId 订单ID
     * @param userId  用户id
     */
    @Override
    public int cancleOrder(Long orderId, Long userId) {
        //1:查询订单是否存在
        UserOrder order = this.getById(orderId);
        if (Vsserts.isNull(order)) {
            return -100;
        }
        // 必须是本人
        if(!order.getUserId().equals(userId)){
            return -101;
        }

        // 为什么要重新创建一个，这是一种按需新的作用
        UserOrder userOrder1 = new UserOrder();
        userOrder1.setId(orderId);
        userOrder1.setStatus(-1);
        this.updateById(userOrder1);
        return  100;
    }


    /**
     * 完成订单
     *
     * @param orderId 订单ID
     * @param userId  用户id
     */
    @Override
    public int finishedOrder(Long orderId, Long userId) {
        //1:查询订单是否存在
        UserOrder order = this.getById(orderId);
        if (Vsserts.isNull(order)) {
            return -100;
        }

        // 必须是本人
        if(!order.getUserId().equals(userId)){
            return -101;
        }

        // 为什么要重新创建一个，这是一种按需新的作用
        UserOrder userOrder1 = new UserOrder();
        userOrder1.setId(orderId);
        userOrder1.setStatus(2);
        this.updateById(userOrder1);
        return  100;
    }

    /**
     * 完成订单
     *
     * @param orderId 订单ID
     * @param userId  用户id
     */
    @Override
    public int finishedOrderOver(Long orderId, Long userId) {
        //1:查询订单是否存在
        UserOrder order = this.getById(orderId);
        if (Vsserts.isNull(order)) {
            return -100;
        }

        // 必须是本人
        if(!order.getUserId().equals(userId)){
            return -101;
        }

        // 为什么要重新创建一个，这是一种按需新的作用
        UserOrder userOrder1 = new UserOrder();
        userOrder1.setId(orderId);
        userOrder1.setStatus(3);
        this.updateById(userOrder1);
        return  100;
    }


    /**
     * 删除订单
     *
     * @param orderId 订单ID
     * @param userId  用户id
     */
    @Override
    public int removeOrder(Long orderId, Long userId) {
        //1:查询订单是否存在
        UserOrder order = this.getById(orderId);
        if (Vsserts.isNull(order)) {
            return -100;
        }

        // 必须是本人
        if(!order.getUserId().equals(userId)){
            return -101;
        }

        // 删除订单
        this.removeById(orderId);
        return 100;
    }


    /**
     * 评论订单
     *
     * @param orderId 订单ID
     * @param userId  用户id
     * @param comment 评价信息
     */
    @Override
    @Transactional
    public int commentOrder(Long orderId, Long userId, String comment) {
        //1:查询订单是否存在
        UserOrder order = this.getById(orderId);
        if (Vsserts.isNull(order)) {
            return -100;
        }

        // 必须是本人
        if(!order.getUserId().equals(userId)){
            return -101;
        }

        // 为什么要重新创建一个，这是一种按需新的作用
        UserOrder userOrder1 = new UserOrder();
        userOrder1.setId(orderId);
        //这里为什么存一份，方便你查看订单直接可以看用户评论
        userOrder1.setCommentInfo(comment);
        userOrder1.setStatus(3);
        this.updateById(userOrder1);

        // 保存酒店评论中去
        HotelComment hotelComment = new HotelComment();
        hotelComment.setContent(comment);
        hotelComment.setHotelId(order.getHotelId());
        hotelComment.setUserid(order.getUserId());
        hotelComment.setUsernickname(order.getNickname());
        hotelComment.setUseravatar(order.getAvatar());
        hotelComment.setParentId(0L);
        hotelComment.setOrderId(orderId);
        hotelComment.setZannum(0);
        hotelCommentService.saveOrUpdate(hotelComment);
        return 100;
    }
}
