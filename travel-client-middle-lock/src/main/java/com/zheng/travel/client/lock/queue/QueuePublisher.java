package com.zheng.travel.client.lock.queue;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class QueuePublisher {


    @Autowired
    private RedissonClient redissonClient;

    /**
     * 发送消息
     */
    @Async
    public void sendMessage(String msg) {
        try {
            // 消息存储的队列的名称
            final String queueName = "TRAVEL_redisson_msg_queue";
            // 获取队列的实列
            RQueue<Object> queue = redissonClient.getQueue(queueName);
            // 把消息添加对队列中
            queue.add(msg);
            log.info("redisson 队列生产消息-,消息发送成功,内容是：{}", msg);
        } catch (Exception ex) {
            log.error("redisson 队列生产消息-发送失败，出现异常：{}", ex.getMessage());
        }
    }


    /**
     * 发送消息 - 延时消息
     */
    public void sendDelayMessage(String msg, Long ttl, TimeUnit timeUnit) {
        try {
            // 消息存储的队列的名称
            final String queueName = "TRAVEL_redisson_delay_msg_queue";
            // 获取队列的实列
            RBlockingQueue<Object> blockingQueue = redissonClient.getBlockingQueue(queueName);
            RDelayedQueue<Object> delayedQueue = redissonClient.getDelayedQueue(blockingQueue);
            // 把消息添加对队列中
            delayedQueue.offer(msg, ttl, timeUnit);
            log.info("redisson 队列生产消息-,消息发送成功,内容是：{}", msg);
        } catch (Exception ex) {
            log.error("redisson 队列生产消息-发送失败，出现异常：{}", ex.getMessage());
        }
    }


}
