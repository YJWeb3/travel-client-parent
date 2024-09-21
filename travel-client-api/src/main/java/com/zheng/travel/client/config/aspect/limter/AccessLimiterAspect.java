package com.zheng.travel.client.config.aspect.limter;

import com.google.common.collect.Lists;
import com.zheng.travel.client.anno.limiter.AccessLimiter;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

@Aspect
@Component
public class AccessLimiterAspect implements Ordered {

    private static final Logger log = LoggerFactory.getLogger(AccessLimiterAspect.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private DefaultRedisScript<Boolean> ipLimitLua;

    // 定义aop的切入点
    @Pointcut("@annotation(com.zheng.travel.client.anno.limiter.AccessLimiter)")
    public void cut() {
        log.info("cut");
    }

    // 拦截属于放入执行之前的行为，所以定义@Before通知
    @Before("cut()")
    public void before(JoinPoint joinPoint) {

        // 获取方法的签名作为key
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        AccessLimiter annotation = method.getAnnotation(AccessLimiter.class);

        //如果没有增加accessLimiter注解说明不需要限流，直接返回
        if (annotation == null) {
            return;
        }

        // 获取限流缓存的key
        String key = annotation.key();
        // 获取限流缓存的限制次数
        Integer limit = annotation.limit();
        // 获取限流缓存的时间
        Integer timeout = annotation.timeout();

        // 如果没有设置key，从调用方法的签名自动生成一个key
        if (StringUtils.isEmpty(key)) {
            // 获取方法所有的参数
            Class<?>[] types = method.getParameterTypes();
            // 获取方法的签名
            key = method.getName();
            // 如果又参数的话，用参数和key来确定唯一
            if (types != null) {
                String paramTypes = Arrays.stream(types).map(Class::getName)
                        .collect(Collectors.joining(","));
                log.info("param types : {}", paramTypes);
                key = key + "#" + paramTypes;
            }
        }

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        HttpServletResponse response = requestAttributes.getResponse();

        // 请求lua脚本
        Boolean acquired = stringRedisTemplate.execute(ipLimitLua,
                Lists.newArrayList(key), limit.toString(), timeout.toString());
        if (!acquired) {
            log.error("you access is blocked ,key:{}", key);
            // 抛出异常，然后让全局异常去处理
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            try(PrintWriter writer = response.getWriter();) {
                response.getWriter().print("<h1>客官你慢点，请稍后在试一试!!!</h1>");
            }catch (Exception ex){
                throw new RuntimeException("客官你慢点，请稍后在试一试!!!");
            }
        }
    }

    @Override
    public int getOrder() {
        return 9999998;
    }
}
