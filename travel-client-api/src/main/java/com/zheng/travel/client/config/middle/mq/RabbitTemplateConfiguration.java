package com.zheng.travel.client.config.middle.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@Slf4j
public class RabbitTemplateConfiguration {

    // 设置RabbitMQ的链接工厂实例
    @Autowired
    private CachingConnectionFactory connectionFactory;


    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RabbitTemplate rabbitTemplate() {
        connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);
        connectionFactory.setPublisherReturns(true);
        // 定义RabbitMQ消息操作组件实例
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        // 设置JSON发送的格式，方便处理消息传递对象
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        // 打开returnConfirm机制回调
        rabbitTemplate.setMandatory(true);
        // 这里是rabbitserver正常和失败都会给你的一个回执确认机制
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                // 1: 如果生产者成功发送消息给rabbitserver，同时还得到确认 ack = true correlationData = null;
                // 2: 如果生产者发送消息给rabbitserver，但是出现异常 同时还得到确认 ack = false， returnedMessage = !null
                if(!ack){
                    log.info("用户下单支付超时 ---------发送消息抵达rabbitserver失败了....,{}",cause);
                    // 1：这里就开始进行告警
                    // 2: 进行重新投递，就考虑到幂等性的问题了
                    //rabbitTemplate.convertAndSend();
                    return;
                }
                // 设置一个开关
                log.info("用户下单支付 ---------发送消息抵达rabbitserver成功了....");
            }
        });

        // 这里是rabbitserver 失败的时候的才会进入的setReturnsCallback
        rabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback() {
            @Override
            public void returnedMessage(ReturnedMessage returned) {
                System.out.println("okokokokok");
            }
        });


        return rabbitTemplate;
    }
}
