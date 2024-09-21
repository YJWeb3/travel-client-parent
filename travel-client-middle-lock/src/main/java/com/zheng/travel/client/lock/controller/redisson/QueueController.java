package com.zheng.travel.client.lock.controller.redisson;

import com.zheng.travel.client.lock.common.R;
import com.zheng.travel.client.lock.common.StatusCode;
import com.zheng.travel.client.lock.queue.QueuePublisher;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
@Api(tags = "基于Redisson - 消息发送")
public class QueueController {

    @Autowired
    private QueuePublisher queuePublisher;


    /**
     * 发送消息
     *
     * @param msg
     * @return
     */
    @GetMapping("/product/send/msg")
    public R sendMessage(String msg) {
        R response = new R(StatusCode.Success);
        try {
            queuePublisher.sendMessage(msg);
        } catch (Exception ex) {
            response = new R(StatusCode.Fail.getCode(), ex.getMessage());
        }
        return response;
    }


    /**
     * 发送消息 -delay
     *
     * @param msg
     * @return
     */
    @GetMapping("/product/send/delay/msg")
    public R sendDelayMessage(String msg) {
        R response = new R(StatusCode.Success);
        try {
            queuePublisher.sendDelayMessage(msg + "1", 1L, TimeUnit.SECONDS);
            queuePublisher.sendDelayMessage(msg + "2", 3L, TimeUnit.SECONDS);
            queuePublisher.sendDelayMessage(msg + "3", 5L, TimeUnit.SECONDS);
        } catch (Exception ex) {
            response = new R(StatusCode.Fail.getCode(), ex.getMessage());
        }
        return response;
    }

}
