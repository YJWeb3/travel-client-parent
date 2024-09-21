package com.zheng.travel.client.anno.limiter;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AccessLimiter {
    // 目标： @AccessLimiter(limit="1",timeout="1",key="user:ip:limit")
    // 解读：一个用户key在timeout时间内，最多访问limit次
    // 缓存的key
    String key() default "";
    // 限制的次数
    int limit() default  5;
    // 过期时间
    int timeout() default  1;
}
