package com.zheng.travel.client.anno.limiter;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AccessIpLimiter {
    // 每timeout限制请求的个数
    int limit() default 10;

    // 时间，单位默认是秒
    int timeout() default 1;

    // 缓存的key
    String key() default "";
}
