package com.zheng.travel.client.threadlocal;


import com.zheng.travel.client.entity.TravelUser;

public class UserThrealLocal {

    // 本地线程缓存: ThreadLocal 底层就是 Map
    private static ThreadLocal<TravelUser> userThreadLocal = new ThreadLocal<TravelUser>();

    public static void put(TravelUser user) {
        userThreadLocal.set(user);
    }

    public static TravelUser get() {
        return userThreadLocal.get();
    }

    public static void remove() {
        userThreadLocal.remove();
    }

}
