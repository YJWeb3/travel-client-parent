package com.zheng.travel.client.config.mvc.interceptor;


import com.zheng.travel.client.constant.TravelContants;
import com.zheng.travel.client.redis.TravelRedisOperator;
import com.zheng.travel.client.result.ex.TravelValidationException;
import com.zheng.travel.client.utils.ip.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class PassportInterceptor implements HandlerInterceptor, TravelContants {

    @Autowired
    private TravelRedisOperator redisOperator;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取用户ip
        String userIp = IpUtils.getIpAddr(request);
        boolean isExist = redisOperator.keyIsExist(TRAVEL_CLIENT_LOGIN_SMS_LIMIT + userIp);
        if (isExist) {
            throw new TravelValidationException(601, "短信发布频率太大");
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
