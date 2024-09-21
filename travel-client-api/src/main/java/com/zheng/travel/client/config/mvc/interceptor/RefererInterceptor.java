package com.zheng.travel.client.config.mvc.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.MalformedURLException;


@Component
public class RefererInterceptor implements HandlerInterceptor {
    /**
     * 白名单
     */
    private String[] refererDomain = new String[]{"localhost","120.77.34.190", "api.txnh.net","www.txnh.net", "www.itbooking.net", "admin.itbooking.net", "admin.txnh.net"};
    /**
     * 是否开启referer校验
     */
    private Boolean check =true;


    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {
        if (!check) {
            return true;
        }
        String referer = req.getHeader("referer");
        String host = req.getServerName();
        // 验证非get请求
        if (!"GET".equals(req.getMethod())) {
            if (referer == null) {
                // 状态置为404
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return false;
            }
            java.net.URL url = null;
            try {
                url = new java.net.URL(referer);
            } catch (MalformedURLException e) {
                // URL解析异常，也置为404
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return false;
            }
            // 首先判断请求域名和referer域名是否相同
            if (!host.equals(url.getHost())) {
                // 如果不等，判断是否在白名单中
                if (refererDomain != null) {
                    for (String s : refererDomain) {
                        if (s.equals(url.getHost())) {
                            return true;
                        }
                    }
                }
                return false;
            }
        }
        return true;
    }
}