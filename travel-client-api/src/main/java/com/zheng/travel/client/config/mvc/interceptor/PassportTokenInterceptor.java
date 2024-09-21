package com.zheng.travel.client.config.mvc.interceptor;

import com.zheng.travel.client.anno.login.LoginCheck;
import com.zheng.travel.client.constant.TravelContants;
import com.zheng.travel.client.localcache.EhCacheService;
import com.zheng.travel.client.redis.TravelRedisOperator;
import com.zheng.travel.client.result.ResultEnum;
import com.zheng.travel.client.result.ex.TravelValidationException;
import com.zheng.travel.client.utils.FastJsonUtil;
import com.zheng.travel.client.utils.fn.asserts.Vsserts;
import com.zheng.travel.client.entity.TravelUser;
import com.zheng.travel.client.threadlocal.UserThrealLocal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class PassportTokenInterceptor implements HandlerInterceptor, TravelContants {

    @Autowired
    private TravelRedisOperator redisOperator;
    @Autowired
    private EhCacheService ehCacheService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            // 让方法的优先级更高
            // 获取方法上面是否存在LoginCheck注解，如果存在并值是false.当然默认值也是false
            LoginCheck methodAnnotation = handlerMethod.getMethodAnnotation(LoginCheck.class);
            // methodAnnotation!=null 代表你加了注解,如果没加就不进入，就说明验证
            // !methodAnnotation.value() 加了如果是false，说明不需要验证了，就直接返回
            // 一句话：在开发中你要么不要加，要么加上去就使用默认值
            if (methodAnnotation!=null && !methodAnnotation.value()) {
                // 用来测试的用户，保证你业务中UserThreadLocal在测试的时候也可以继续使用
                TravelUser TravelUser = new TravelUser();
                TravelUser.setId(1511758403479158785L);
                TravelUser.setNickname("test用户");
                // 放入本地线程中
                UserThrealLocal.put(TravelUser);

                return true;
            }

            // 然后就是类的优先级
            Class<?> beanType = handlerMethod.getBeanType();
            LoginCheck annotation = beanType.getAnnotation(LoginCheck.class);
            if (annotation!=null && !annotation.value()) {
                // 用来测试的用户，保证你业务中UserThreadLocal在测试的时候也可以继续使用
                TravelUser TravelUser = new TravelUser();
                TravelUser.setId(1511758403479158785L);
                TravelUser.setNickname("test用户");
                // 放入本地线程中
                UserThrealLocal.put(TravelUser);

                return true;
            }
        }

        // 1: 获取前台接口调用请求头中的用户和token参数
        String authUserId = request.getHeader("AuthUserId");
        String authToken = request.getHeader("AuthToken");

        // 2: 如果token和userId是空的，就直接返回
        if (Vsserts.isNotEmpty(authUserId) && Vsserts.isNotEmpty(authToken)) {
            // 根据用户id查询缓存中的token
            String token = (String)ehCacheService.getCache(TRAVEL_CLIENT_LOGIN_USER_TOKEN + authUserId);
            if(Vsserts.isEmpty(token)) {
                token = redisOperator.get(TRAVEL_CLIENT_LOGIN_USER_TOKEN + authUserId);
            }
            // 如果是空，就说明退出了。
            if (Vsserts.isEmpty(token)) {
                //直接在前台响应地方跳转到登录去
                throw new TravelValidationException(ResultEnum.LOGIN_FAIL);
            }
            // 如果不是空，说明用户前台app或者小程序传递了token，
            // 把传递进来的token和缓存中的token进行比较看是否一致
            // google浏览器：说明它肯定退出一次然后又登录了 user:1 token 545454545
            // firefox浏览器：说明它肯定退出一次然后又登录了 user:1 token 878787878
            if (!token.equalsIgnoreCase(authToken)) {
                throw new TravelValidationException(ResultEnum.SAME_LOGIN);
            }
        } else {
            throw new TravelValidationException(ResultEnum.LOGIN_FAIL);
        }

        // 从缓存中获取用户信息
        TravelUser TravelUser = (TravelUser)ehCacheService.getCache(TRAVEL_CLIENT_LOGIN_USER_INFO + authUserId);
        if(Vsserts.isNull(TravelUser)) {
            String userCacheJson = redisOperator.get(TRAVEL_CLIENT_LOGIN_USER_INFO + authUserId);
            TravelUser = FastJsonUtil.toBean(userCacheJson, TravelUser.class);
        }

        // 放入本地线程中
        UserThrealLocal.put(TravelUser);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        UserThrealLocal.remove();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserThrealLocal.remove();
    }
}
