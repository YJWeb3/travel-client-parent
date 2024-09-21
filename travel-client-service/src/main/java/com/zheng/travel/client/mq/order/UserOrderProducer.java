package com.zheng.travel.client.mq.order;

import com.zheng.travel.client.redis.TravelRedisOperator;
import com.zheng.travel.client.dto.UserOrderDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.AbstractJavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserOrderProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private TravelRedisOperator redisOperator;

    /**
     * @param userOrderDto
     */
    public void pusherOrder(UserOrderDto userOrderDto) {
        try {
            // 1: 设置对象转码 覆盖全局，更更改
            rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
            // 2: 设置交换机
            rabbitTemplate.setExchange(OrderTopicDeadConfiguration.TRAVEL_ORDER_TOPIC_EXCHANGE);
            // 3: 设置路由key
            rabbitTemplate.setRoutingKey(OrderTopicDeadConfiguration.TRAVEL_ORDER_TOPIC_ROUTING_KEY);
            // 4: 发送消息，设置消息附属信息，信息载体和消息持久化
            rabbitTemplate.convertAndSend(userOrderDto, new MessagePostProcessor() {
                @Override
                public Message postProcessMessage(Message message) throws AmqpException {
                    // 获取消息属性对象
                    MessageProperties messageProperties = message.getMessageProperties();
                    // 设置消息的持久化策略
                    messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                    // 设置消息头的属性的对象
                    messageProperties.setHeader(AbstractJavaTypeMapper.DEFAULT_CONTENT_CLASSID_FIELD_NAME, UserOrderDto.class);
                    // 设置额外的参数
                    messageProperties.setHeader("msgtype", "1");
                    // 设置时间
                    //messageProperties.setExpiration("5000");
                    // 返回消息实列
                    return message;
                }
            });
            // 5: 打印日志
            log.info("用户下单支付 - 发送用户下单死信队列的内容是：{}", userOrderDto);
            // 6：可靠生产问题
            // - 确认机制
            // - 错误处理
        } catch (Exception ex) {
            log.info("用户下单支付 - 发送用户下单死信队列出现异常，出现异常的内容是：{}", userOrderDto);
        }
    }
}
