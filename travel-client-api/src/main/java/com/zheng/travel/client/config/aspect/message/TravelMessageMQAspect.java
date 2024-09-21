package com.zheng.travel.client.config.aspect.message;

import com.zheng.travel.client.anno.log.TravelMQMessage;
import com.zheng.travel.client.mq.message.MessageMQProducerService;
import com.zheng.travel.client.utils.fn.asserts.Vsserts;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
@Aspect
@Slf4j
public class TravelMessageMQAspect /*implements ApplicationContextAware*/ {

    @Autowired
    private MessageMQProducerService messageProducerService;


    // 定义aop的切入点
    @Pointcut("@annotation(com.zheng.travel.client.anno.log.TravelMQMessage)")
    public void messagecut() {

    }

    /**
     * 消息处理的位置
     */
    @AfterReturning(value = "messagecut()",returning = "methodReturn")
    public void message(JoinPoint joinPoint,Object methodReturn) {
        // 1：获取方法的签名作为key
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        TravelMQMessage annotation = method.getAnnotation(TravelMQMessage.class);
        //2：如果没有增加accessLimiter注解说明不需要限流，直接返回
        if (annotation == null) {
            return;
        }

        try {
            String routingkey = annotation.routingkey();//user.mongo.message
            if (Vsserts.isEmpty(routingkey)) {
                return;
            }
            // 1 :开始进行远程发送消息到MQ
            messageProducerService.saveMQMessage(methodReturn, routingkey);
        } catch (Exception ex) {
            ex.printStackTrace();
            log.info("执行日志拦截出错了..............");
        }
    }
}
