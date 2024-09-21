package com.zheng.travel.client.lock.queue;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Slf4j
public class QueueConsummer2 implements ApplicationRunner, Ordered {


    @Autowired
    private RedissonClient redissonClient;


    /**
     * 这个方式是指springboot在启动成功之后加载的一个方法
     *
     * @param applicationArguments
     * @throws Exception
     */
    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        log.info("delay ----->消费消息进来了......");
        // 1: 指定你消费的队列
        final String queueName = "TRAVEL_redisson_delay_msg_queue";
        // 2: 获取队列的列表信息
        RQueue<String> rQueue = redissonClient.getQueue(queueName);
        // 死循环不停的去监听队列释放存在消息，存在就消费掉
        while (true) {
            String msg = rQueue.poll();
            if (!StringUtils.isEmpty(msg)) {
                log.info("delay ----->队列消费者监听消息的内容是：{}", msg);
                // 比如：发送es/lostassh/mq/webscoket
            }
        }
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
