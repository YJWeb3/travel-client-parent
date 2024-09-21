package com.zheng.travel.client.message.service;

import com.zheng.travel.client.message.config.ConsumerMessageTopicMQConfiguration;
import com.zheng.travel.client.message.mongoservice.message.IMessageService;
import com.zheng.travel.client.message.mongo.OrderMessageMo;
import com.zheng.travel.client.message.mongo.UserMessageMo;
import com.zheng.travel.client.message.mongoservice.ordermessage.IOrderMessageService;
import com.zheng.travel.client.message.mongoservice.usermessage.IUserMessageService;
import com.zheng.travel.client.message.util.FastJsonUtil;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;

@Component
@Slf4j
public class MessageConsumer {

    @Autowired
    private IMessageService messageService;
    @Autowired
    private IOrderMessageService orderMessageService;
    @Autowired
    private IUserMessageService userMessageService;

    @RabbitListener(queues = {ConsumerMessageTopicMQConfiguration.TRAVEL_MESSAGE_TOPIC_MONGGO_QUEUE},
            containerFactory = "simpleTopicMessageContainerMununal")
    public void mongoMessage(@Payload Map<String, Object> objectMap, Message message, Channel channel) {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        String receivedRoutingKey = message.getMessageProperties().getReceivedRoutingKey();
        try {
            if(null != objectMap) {
                String msgtype = String.valueOf(objectMap.get("msgtype"));
                if(!StringUtils.isEmpty(msgtype)) {
                    String toJSON = FastJsonUtil.toJSON(objectMap);
                    if (msgtype.equals("1")) {
                        UserMessageMo userMessageMo = FastJsonUtil.toBean(toJSON, UserMessageMo.class);
                        userMessageService.saveMessage(userMessageMo);
                        log.info("TRAVEL user message mongodb  save {} routingkey :{} ...............", userMessageMo,receivedRoutingKey);
                    } else if (msgtype.equals("2")) {
                        OrderMessageMo orderMessageMo = FastJsonUtil.toBean(toJSON, OrderMessageMo.class);
                        orderMessageService.saveMessage(orderMessageMo);
                        log.info("TRAVEL order message mongodb  save {} routingkey :{} ...............", orderMessageMo,receivedRoutingKey);
                    }
                    channel.basicAck(deliveryTag, false);
                }
            }
        } catch (Exception ex) {
            try {
                log.info("出现异常，可以考虑进行重试....");
                channel.basicReject(deliveryTag, true);
            } catch (Exception ex2) {
                ex2.printStackTrace();
            }
        }
    }

    @RabbitListener(queues = {ConsumerMessageTopicMQConfiguration.TRAVEL_MESSAGE_TOPIC_ES_QUEUE},
            containerFactory = "simpleTopicMessageContainerMununal")
    public void esMessage(@Payload Map<String,Object> objectMap, Message message, Channel channel) {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        String receivedRoutingKey = message.getMessageProperties().getReceivedRoutingKey();
        try {
            if(null != objectMap) {
                String msgtype = String.valueOf(objectMap.get("msgtype"));
                if(!StringUtils.isEmpty(msgtype)) {
                    String toJSON = FastJsonUtil.toJSON(objectMap);
                    if (msgtype.equals("1")) {
                        UserMessageMo userMessageMo = FastJsonUtil.toBean(toJSON, UserMessageMo.class);
                        // es save
                        log.info("TRAVEL user message es  save {} routingkey :{} ...............", userMessageMo,receivedRoutingKey);
                    } else if (msgtype.equals("2")) {
                        OrderMessageMo orderMessageMo = FastJsonUtil.toBean(toJSON, OrderMessageMo.class);
                        // es save
                        log.info("TRAVEL order message es  save {} routingkey :{} ...............", orderMessageMo,receivedRoutingKey);
                    }
                    channel.basicAck(deliveryTag, false);
                }
            }
        } catch (Exception ex) {
            try {
                log.info("出现异常，可以考虑进行重试....");
                channel.basicReject(deliveryTag, true);
            } catch (Exception ex2) {
                ex2.printStackTrace();
            }
        }
    }
}
