package com.zheng.travel.client.utils.cookie;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {

    // private final static String COOKIE_DOMAIN = ".itbooking.com";
    public final static String LOGIN_COOKIE_NAME = "xten_login_token";
    public final static String LOGIN_COOKIE_NAME_ID = "xten_login_token_id";



    public static String readLoginToken(String COOKIE_NAME,HttpServletRequest request) {

        Cookie[] cks = request.getCookies();
        if (cks != null) {
            for (Cookie ck : cks) {
                if (ck.getName().equals(COOKIE_NAME)) {
                    return ck.getValue();
                }
            }
        }
        return null;
    }

    public static void writeLoginToken(HttpServletResponse response, String COOKIE_NAME,String token) {
        Cookie ck = new Cookie(COOKIE_NAME, token);
        // ck.setDomain(COOKIE_DOMAIN);
        ck.setPath("/");// 代表设置在根目录
        ck.setHttpOnly(true);
        // 如果这个maxage不设置的话，cookie就不会写入硬盘，而是写在内存。只在当前页面有效。
        ck.setMaxAge(60 * 60 * 24 * 7);// 如果是-1，代表永久
        response.addCookie(ck);
    }


    public static void delLoginToken(String COOKIE_NAME,HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cks = request.getCookies();
        if (cks != null) {
            for (Cookie ck : cks) {
                if (ck.getName().equals(COOKIE_NAME)) {
                    // ck.setDomain(COOKIE_DOMAIN);
                    ck.setPath("/");
                    ck.setMaxAge(0);// 设置成0，代表删除此cookie。
                    response.addCookie(ck);
                    return;
                }
            }
        }
    }



}
