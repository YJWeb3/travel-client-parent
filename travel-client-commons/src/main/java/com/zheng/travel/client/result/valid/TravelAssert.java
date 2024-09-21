package com.zheng.travel.client.result.valid;

import com.zheng.travel.client.result.ResultEnum;
import com.zheng.travel.client.result.ex.TravelValidationException;

public class TravelAssert {

    public static void isEmpty(Object object,Integer code,String message) {
        if (object == null || "".equals(object)) {
            throw new TravelValidationException(code,message);
        }
    }

    public static void isEmpty(Object object, ResultEnum resultEnum) {
        if (object == null || "".equals(object)) {
            throw new TravelValidationException(resultEnum);
        }
    }
}
