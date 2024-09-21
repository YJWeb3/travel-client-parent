package com.zheng.travel.client.mq.message;

import com.zheng.travel.client.bo.TravelUserBo;
import com.zheng.travel.client.utils.FastJsonUtil;
import com.zheng.travel.client.utils.date.TmDateUtil;
import com.zheng.travel.client.entity.UserOrder;
import com.zheng.travel.client.mongo.OrderMessageMo;
import com.zheng.travel.client.mongo.UserMessageMo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Map;

@Service
@Slf4j
public class MessageMQProducerService {

    @Autowired
    private RabbitTemplate rabbitTemplate;


    public Object getMessageMo(Object methodResult) {
        Object parameter = null;
        // 如果方法是无返回值就不处理了
        if (null == methodResult) {
            return null;
        }

        if (methodResult instanceof TravelUserBo) {
            TravelUserBo TravelUserBo = (TravelUserBo) methodResult;
            // 发送消息
            UserMessageMo userMessageMo = new UserMessageMo();
            userMessageMo.setTitle("注册通知");
            userMessageMo.setMsgcontent("恭喜你【" + TravelUserBo.getNickname() + "】加入到旅游平台，奖励积分50。");
            userMessageMo.setMsgtype(1);
            userMessageMo.setOpid(TravelUserBo.getId());
            userMessageMo.setCreateTime(TmDateUtil.getCurrentTime());
            userMessageMo.setNickname(TravelUserBo.getNickname());
            userMessageMo.setUserid(TravelUserBo.getId() + "");
            userMessageMo.setAvatar(TravelUserBo.getAvatar());
            userMessageMo.setIcon("/static/icon-item-001.png");
            return userMessageMo;
        } else if (methodResult instanceof UserOrder) {
            UserOrder order = (UserOrder) methodResult;
            // 发送消息
            OrderMessageMo orderMessageMo = new OrderMessageMo();
            orderMessageMo.setTitle("订单通知");
            orderMessageMo.setMsgcontent("用户【" + order.getNickname() + "】您好，" +
                    "你已经成功入住【" + order.getHotelTitle() + "_" + order.getHotelDetailTitle() + "】 " +
                    "\n 酒店的地址:" + order.getHotelAddress() + " \n联系方式是：" + order.getHotelTel() + "。");
            orderMessageMo.setMsgtype(2);
            orderMessageMo.setOpid(order.getId());
            orderMessageMo.setCreateTime(TmDateUtil.getCurrentTime());
            orderMessageMo.setNickname(order.getNickname());
            orderMessageMo.setUserid(order.getUserId() + "");
            orderMessageMo.setAvatar(order.getAvatar());
            orderMessageMo.setIcon("/static/icon-item-002.png");
            return orderMessageMo;
        }

        return null;

    }


    /**
     * 远程发送的方法
     *
     * @param messageObj
     * @param routeKey
     */
    public void saveMQMessage(Object messageObj, String routeKey) {
        Object messageMo = getMessageMo(messageObj);
        // 进行消息保存可能不用 先打开它可以保证可靠生产，也就是如果发送消息失败了 + setMandatory(true)
        // 就会进入到setRetureCallback进行确认
        rabbitTemplate.setMandatory(true);
        // 执行交换机
        rabbitTemplate.setExchange(MessageTopicMQConfiguration.TRAVEL_MESSAGE_TOPIC_EXCHANGE);
        // 指定路由key
        rabbitTemplate.setRoutingKey(routeKey);
        /// 开始发送消息
        String s = FastJsonUtil.toJSON(messageMo);
        Map<String, Object> map = (Map<String, Object>) FastJsonUtil.toMap(s);
        rabbitTemplate.convertAndSend(map, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                MessageProperties messageProperties = message.getMessageProperties();
                // 消息持久化,防止rabbitmq服务宕机重启以后message依然存在
                messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                // 设置一些额外的参数
                //messageProperties.setHeader("msgtype", "1");
                // 设置消息的时间 好处：可以防止消息拥堵 单位是：ms
                //messageProperties.setExpiration("10000");
                return message;
            }
        });

        log.info("TRAVEL send message to mongodb succes ,{}", messageObj);
    }

}
