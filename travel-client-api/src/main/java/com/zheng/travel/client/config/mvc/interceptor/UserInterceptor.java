package com.zheng.travel.client.config.mvc.interceptor;//package com.CLIENT.config.handler;

import com.zheng.travel.client.result.R;
import com.zheng.travel.client.utils.JsonUtil;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class UserInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String method = request.getMethod();
        if (method.equalsIgnoreCase("POST") || method.equalsIgnoreCase("DELETE")
                || method.equalsIgnoreCase("PUT")) {
            String csrf_token = request.getParameter("csrf_token");
            Cookie[] cookies = request.getCookies();
            if (cookies != null && cookies.length > 0 && csrf_token != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("csrf_token")) {
                        if (Integer.valueOf(csrf_token) == cookie.getValue().hashCode()) {
                            return true;
                        }
                    }
                }
            }
        }
        PrintWriter out = response.getWriter();
        out.write(JsonUtil.obj2String(R.fail(403, "你还想攻击我...")));
        out.close();
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}


