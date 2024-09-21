package com.zheng.travel.client.anno.log;


import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TravelMQMessage {

    /**
     * 传递具体的事件类型
     * @return
     */
    String routingkey() default  "es.default";

    /**
     * 默认value
     * @return
     */
    String value() default "";
}
