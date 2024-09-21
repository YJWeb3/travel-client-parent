package com.zheng.travel.client.config.mq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class OrderTopicDeadConfiguration {

    /*整体的消息模型是：topic/direct */

    /*生产者 交换机，路由key */
    public static final String TRAVEL_ORDER_TOPIC_EXCHANGE = "TRAVEL.order.topic.exchange";
    public static final String TRAVEL_ORDER_TOPIC_ROUTING_KEY = "TRAVEL.order.topic.routing.key";

    /*死信队列 死信交换机，死信路由key*/
    public static final String TRAVEL_ORDER_TOPIC_DEAD_EXCHANGE = "TRAVEL.order.topic.dead.exchange";
    public static final String TRAVEL_ORDER_TOPIC_DEAD_ROUTING_KEY = "TRAVEL.order.topic.dead.routing.key";
    public static final String TRAVEL_ORDER_TOPIC_DEAD_QUEUE = "TRAVEL.order.topic.dead.queue";

    /*消费者队列*/
    public static final String TRAVEL_ORDER_TOPIC_CONSUMER_QUEUE = "TRAVEL.order.topic.consumer.queue";


    /**
     * 创建生产者的交换机
     * @return
     */
    @Bean
    public Exchange createTopicOrderExchange(){
        return ExchangeBuilder.topicExchange(TRAVEL_ORDER_TOPIC_EXCHANGE).durable(true).build();
    }


    /**
     * 创建死信的交换机的交换机
     * @return
     */
    @Bean
    public Exchange createTopicOrderDeadExchange(){
        return ExchangeBuilder.topicExchange(TRAVEL_ORDER_TOPIC_DEAD_EXCHANGE).durable(true).build();
    }


    @Bean
    public Queue createConsumeQueue(){
        return QueueBuilder.durable(TRAVEL_ORDER_TOPIC_CONSUMER_QUEUE).build();
    }

    /**
     * 创建死信队列
     * @return
     */
    @Bean
    public Queue createTopicOrderDeadQueue(){
        // 使用Map存放死信队列的三个核心组成部分
        Map<String, Object> args = new HashMap<>();
        // 创建死信队列交换机
        args.put("x-dead-letter-exchange", TRAVEL_ORDER_TOPIC_DEAD_EXCHANGE);
        // 创建死信队列路由
        args.put("x-dead-letter-routing-key", TRAVEL_ORDER_TOPIC_DEAD_ROUTING_KEY);
        // 设定TTL，单位是ms，下面的单位是10s
        args.put("x-message-ttl", 1000 * 60 * 30);
        // 创建队列并返回死信队列实例
        return QueueBuilder.durable(TRAVEL_ORDER_TOPIC_DEAD_QUEUE).withArguments(args).build();
    }




    /**
     * 把正常的队列和正常的路由key+绑定到死信队列
     * @return
     */
    @Bean
    public Binding bingdingTopicOrderExDead(){
        return BindingBuilder.bind(createTopicOrderDeadQueue())
                .to(createTopicOrderExchange()).with(TRAVEL_ORDER_TOPIC_ROUTING_KEY).noargs();
    }


    /**
     * 把正常的队列和正常的路由key+绑定到死信队列
     * @return
     */
    @Bean
    public Binding bingdingTopicOrderConsumer(){
        return BindingBuilder.bind(createConsumeQueue())
                .to(createTopicOrderDeadExchange()).with(TRAVEL_ORDER_TOPIC_DEAD_ROUTING_KEY).noargs();
    }




    // 设置RabbitMQ的链接工厂实例
    @Autowired
    private CachingConnectionFactory connectionFactory;

    /**
     * 单一消费者，确认模式是：Auto - ACK
     */
    @Bean("simpleTopicOrderDeadContainerAuto")
    public SimpleRabbitListenerContainerFactory simpleTopicOrderDeadContainerAuto(){
        // 创建消息监听器所在的容器工厂实例
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        // 为容器工厂实例设置链接工厂
        factory.setConnectionFactory(connectionFactory);
        // 设置消息在传输过程中的格式为JSON
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        // 设置消息并发实例,这里采用单一模式
        factory.setConcurrentConsumers(1);
        // 设置消费者并发最大数量实例
        factory.setMaxConcurrentConsumers(1);
        // 设置消费每个并发实例预拉取的消息数据量
        factory.setPrefetchCount(1);
        // ACK自动模型
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
        // 返回监听器容器工厂实例
        return factory;
    }


    /**
     * 单一消费者，确认模式是：MANUAL- ACK
     */
    @Bean("simpleTopicOrderDeadContainerMunual")
    public SimpleRabbitListenerContainerFactory simpleTopicOrderDeadContainerMunual(){
        // 创建消息监听器所在的容器工厂实例
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        // 为容器工厂实例设置链接工厂
        factory.setConnectionFactory(connectionFactory);
        // 设置消息在传输过程中的格式为JSON
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        // 设置消息并发实例,这里采用单一模式
        factory.setConcurrentConsumers(1);
        // 设置消费者并发最大数量实例
        factory.setMaxConcurrentConsumers(1);
        // 设置消费每个并发实例预拉取的消息数据量
        factory.setPrefetchCount(1);
        // ACK自动模型
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        // 返回监听器容器工厂实例
        return factory;
    }



}
