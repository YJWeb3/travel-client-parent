package com.zheng.travel.client.anno.log;

import com.zheng.travel.client.event.MongoEvent;

import java.lang.annotation.*;


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TravelMessage {

    /**
     * 传递具体的事件类型
     * @return
     */
    Class[] eventClasses() default  {MongoEvent.class};

    /**
     * 默认value
     * @return
     */
    String value() default "";
}
