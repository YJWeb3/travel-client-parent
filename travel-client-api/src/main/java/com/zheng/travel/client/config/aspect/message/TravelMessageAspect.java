package com.zheng.travel.client.config.aspect.message;

import com.zheng.travel.client.anno.log.TravelMessage;
import com.zheng.travel.client.event.EsEvent;
import com.zheng.travel.client.event.MongoEvent;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
@Aspect
@Slf4j
public class TravelMessageAspect {

    @Autowired
    private ApplicationContext applicationContext;

    // 定义aop的切入点
    @Pointcut("@annotation(com.zheng.travel.client.anno.log.TravelMessage)")
    public void messagecut() {}



    /**
     * 消息处理的位置
     */
    @AfterReturning(value = "messagecut()", returning = "methodResult")
    public void message(JoinPoint joinPoint,Object methodResult) {
        // 1：获取方法的签名作为key
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        TravelMessage annotation = method.getAnnotation(TravelMessage.class);

        //2：如果没有增加accessLimiter注解说明不需要限流，直接返回
        if (annotation == null) {
            return;
        }

        try {
            Class[] eventClasses = annotation.eventClasses();
            if (eventClasses != null && eventClasses.length > 0) {
                for (Class eventClass : eventClasses) {
                    // 1：获取方法的参数
                    Object[] parameters = joinPoint.getArgs();
                    //3：否则就要处理消息
                    if (eventClass == MongoEvent.class) {
                        MongoEvent mongoEvent = new MongoEvent(applicationContext);
                        mongoEvent.setParameter(parameters);
                        mongoEvent.setMethodResult(methodResult);
                        applicationContext.publishEvent(mongoEvent);//1-N
                        // 发送消息到mongodb
                    } else if (eventClass == EsEvent.class) {
                        EsEvent esEvent = new EsEvent(applicationContext);
                        esEvent.setParameter(parameters);
                        esEvent.setMethodResult(methodResult);
                        applicationContext.publishEvent(esEvent);//1-N
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            log.info("执行日志拦截出错了..............");
        }
    }
}
