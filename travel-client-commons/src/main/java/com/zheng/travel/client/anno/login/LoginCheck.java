package com.zheng.travel.client.anno.login;

import java.lang.annotation.*;


@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LoginCheck {

    boolean value() default false;
}
