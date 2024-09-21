package com.zheng.travel.client.utils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class WebUtils {

    /**
     * 将字符串渲染到客户端
     *
     * @param response 渲染对象
     * @param string   待渲染的字符串
     * @return null
     */
    public static String renderString(HttpServletResponse response, String string) {
        response.setStatus(200);
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("utf-8");
        try (ServletOutputStream output = response.getOutputStream();) {
            output.write(string.getBytes());
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}