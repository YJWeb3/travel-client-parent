package com.zheng.travel.client.result.ex;


import com.zheng.travel.client.result.ResultEnum;

public class TravelBusinessException extends RuntimeException {
    private Integer code;
    private String msg;
    public TravelBusinessException(ResultEnum resultEnum) {
        this.code = resultEnum.getCode();
        this.msg = resultEnum.getMessage();
    }
    public TravelBusinessException(Integer code, String message) {
        this.code = code;
        this.msg = message;
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