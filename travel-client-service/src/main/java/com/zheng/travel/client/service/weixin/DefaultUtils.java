package com.zheng.travel.client.service.weixin;

import org.springframework.util.StringUtils;

public class DefaultUtils {

    public static <T> T defaultValue(T value, T defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    public static String defaultSValue(String value, String defaultValue) {
        if (StringUtils.isEmpty(value)) {
            return defaultValue;
        }
        return value;
    }
}
