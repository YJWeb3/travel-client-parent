package com.zheng.travel.client.result.ex;

import com.zheng.travel.client.result.ResultEnum;

public class TravelValidationException extends RuntimeException {
    private Integer code;
    private String msg;
    public TravelValidationException(ResultEnum resultEnum) {
        this.code = resultEnum.getCode();
        this.msg = resultEnum.getMessage();
    }

    public TravelValidationException(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}