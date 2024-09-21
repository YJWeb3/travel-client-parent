package com.zheng.travel.client.mq;

import com.zheng.travel.client.config.mq.OrderTopicDeadConfiguration;
import com.zheng.travel.client.dto.UserOrderDto;
import com.zheng.travel.client.service.IUserOrderService;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
@Slf4j
public class OrderDeadConsumer {

    @Autowired
    private IUserOrderService userOrderService;

    @RabbitListener(queues = {OrderTopicDeadConfiguration.TRAVEL_ORDER_TOPIC_CONSUMER_QUEUE},
            containerFactory = "simpleTopicOrderDeadContainerMunual")
    public void consumerOrder(@Payload UserOrderDto userOrderDto, Message message, Channel channel) throws Exception {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            // 处理用户订单状态，修改 -1， 代表订单已经过期
            userOrderService.updateUserOrderStatus(userOrderDto);
            // 手动应答
            channel.basicAck(deliveryTag, false);
            log.info("用户下单支付超时消息模型 - 监听真正队列，监听到的订单内容是：{}", userOrderDto);
        } catch (Exception ex) {
            // 1: 拿到消息的头部
            Map<String, Object> headers = message.getMessageProperties().getHeaders();
            // 2: 重试次数
            Integer retryCount;
            String mapKey = "retry-count";
            if (!headers.containsKey(mapKey)) {
                retryCount = 0;
            } else {
                retryCount = (Integer) headers.get(mapKey);
            }
            // 3：代表重试三次
            if (retryCount++ < 3) {
                log.info("已经重试 " + retryCount + " 次");
                headers.put("retry-count", retryCount);
                //当消息回滚到消息队列时，这条消息不会回到队列尾部，而是仍是在队列头部。
                //这时消费者会立马又接收到这条消息进行处理，接着抛出异常，进行 回滚，如此反复进行
                //而比较理想的方式是，出现异常时，消息到达消息队列尾部，这样既保证消息不回丢失，又保证了正常业务的进行。
                //因此我们采取的解决方案是，将消息进行应答。
                //这时消息队列会删除该消息，同时我们再次发送该消息 到消息队列，这时就实现了错误消息进行消息队列尾部的方案
                //1.应答--- 就是代表把rabbit-server中的消息剔除掉
                channel.basicAck(deliveryTag, false);
                //2.重新发送到MQ中
                AMQP.BasicProperties basicProperties = new AMQP.BasicProperties().builder()
                        .contentType("application/json").headers(headers).build();
                // 重新把消息发动自己的交换机和队列中继续消费
                channel.basicPublish(message.getMessageProperties().getReceivedExchange(),
                        message.getMessageProperties().getReceivedRoutingKey(), basicProperties,
                        message.getBody());
            } else {
                log.info("现在重试次数为：" + retryCount);
                /**
                 * 重要的操作 存盘
                 * 手动ack
                 * channel.basicAck(deliveryTag,false);//正常应答
                 * channel.basicReject(deliveryTag,false);//单应答 + 重新投递
                 * channel.basicNack(deliveryTag, false, false);//批量应答 + 重新投递
                 * 通知人工处理
                 * log.error("重试三次异常，快来人工处理");
                 */
                // MsgLog msgLog = new MsgLog();
                // msgLog.setMsgId(msg.getMsgId());
                // msgLog.setMsg(new String(message.getBody(),"utf-8"));
                // msgLog.setExchange(message.getMessageProperties().getReceivedExchange());
                // msgLog.setRoutingKey(message.getMessageProperties().getReceivedRoutingKey());
                // msgLog.setTryCount(retryCount);
                // msgLog.setStatus(MsgLogStatusEnum.FAIL.getStatus());
                // msgLogService.save(msgLog);

                /**
                 * 不重要的操作放入 死信队列
                 * 消息异常处理：消费出现异常后，延时几秒，然后从新入队列消费，直到达到ttl超时时间，再转到死信，证明这个信息有问题需要人工干预
                 */
                //休眠2s 延迟写入队列，触发转入死信队列
                //Thread.sleep(2000);
                channel.basicNack(deliveryTag, false, false);
                //channel.basicRecover(false);
            }

            log.error("用户下单支付超时消息模型 - 监听真正队列，出现异常：{}", userOrderDto);
        }
    }
}
