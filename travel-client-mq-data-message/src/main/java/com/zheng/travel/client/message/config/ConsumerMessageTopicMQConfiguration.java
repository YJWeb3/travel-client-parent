package com.zheng.travel.client.message.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ConsumerMessageTopicMQConfiguration {


    /*
     * 根据RabbitMQ遵循的AMQP协议的规范：如果你要完成RabbitMQ消息发送和监听必须要有如下几个角色
     * 1: 生成者
     * 2: RabbitServer ---Brokeer
     * 3: Connection
     * 4: Channel
     * ----------------------------------RabbitTemplate--------------
     * 5: Exchange
     * 6: RoutingKey
     * 7: Binding
     * 8: Queueu
     * 9: 消费者
     * */

    // 定义交换机Exchange
    public static final String TRAVEL_MESSAGE_TOPIC_EXCHANGE = "TRAVEL.message.topic.ex";

    // 定义MongoDB RoutingKey
    public static final String TRAVEL_MESSAGE_TOPIC_MONGO_ROUTING_KEY = "*.mongo.*";//优化处理 m.mongo.order
    // 定义MongoDb队列
    public static final String TRAVEL_MESSAGE_TOPIC_MONGGO_QUEUE = "TRAVEL.message.topic.mongo.queue";

    // 定义ES RoutingKey
    public static final String TRAVEL_MESSAGE_TOPIC_ES_ROUTING_KEY = "es.#";//优化处理es.mongo
    // 定义ES队列
    public static final String TRAVEL_MESSAGE_TOPIC_ES_QUEUE = "TRAVEL.message.topic.es.queue";


    /**
     * 创建交换机
     * @return
     */
    @Bean
    public Exchange messageTopicExchange(){
        return ExchangeBuilder.topicExchange(TRAVEL_MESSAGE_TOPIC_EXCHANGE).durable(true).build();
    }


    /**
     * 创建Mongodb的队列
     * @return
     */
    @Bean
    public Queue messageTopicMongoQueue(){
        return QueueBuilder.durable(TRAVEL_MESSAGE_TOPIC_MONGGO_QUEUE).build();
    }


    /**
     * 创建ES的队列
     * @return
     */
    @Bean
    public Queue messageTopicESQueue(){
        return QueueBuilder.durable(TRAVEL_MESSAGE_TOPIC_ES_QUEUE).build();

    }


    /**
     * 将mongodb的queue通过路由key和exchange绑定
     * 这样当程序通过rabbittemplate.convertAndSend(ex,key,“我爱你”)
     * @return
     */
    @Bean
    public Binding messageTopicMongoBinding(){
        return BindingBuilder.bind(messageTopicMongoQueue()).to(messageTopicExchange())
                .with(TRAVEL_MESSAGE_TOPIC_MONGO_ROUTING_KEY).noargs();
    }


    /**
     * 将es的queue通过路由key和exchange绑定
     * 这样当程序通过rabbittemplate.convertAndSend(ex,key,“我爱你”)
     * @return
     */
    @Bean
    public Binding messageTopicESBinding(){
        return BindingBuilder.bind(messageTopicESQueue()).to(messageTopicExchange())
                .with(TRAVEL_MESSAGE_TOPIC_ES_ROUTING_KEY).noargs();
    }


    // 设置RabbitMQ的链接工厂实例
    @Autowired
    private CachingConnectionFactory connectionFactory;

    /**
     * 单一消费者，确认模式是：Auto - ACK
     */
    @Bean("simpleTopicMessageContainerAuto")
    public SimpleRabbitListenerContainerFactory simpleTopicMessageContainerAuto(){
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
    @Bean("simpleTopicMessageContainerMununal")
    public SimpleRabbitListenerContainerFactory simpleTopicMessageContainerMununal(){
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
