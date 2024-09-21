package com.zheng.travel.client.result;

public class R<T> {

    // 返回的编号
    private Integer code;
    // 返回的数据,数据类型N中，
    private T data;
    // 返回的信息
    private Object message;

    // 调用过程中，保持一种调用。直接用类去调用。
    private R() {

    }

    /**
     * @return com.kuangstudy.common.R
     * @Author xuke
     * @Description 成功返回
     * @Date 21:55 2021/6/23
     * @Param []
     **/
    public static <T> R<T> success(T data, String message) {
        R r = new R();
        r.setCode(ResultEnum.SUCCESS.getCode());
        r.setData(data);
        r.setMessage(message == null ? ResultEnum.SUCCESS.getMessage() : message);
        return r;
    }

    public static R success(){
        R r = new R();
        r.setCode(ResultEnum.SUCCESS.getCode());
        r.setMessage(ResultEnum.SUCCESS.getMessage());
        return r;
    }


    /**
     * @return com.kuangstudy.common.R
     * @Author xuke
     * @Description 成功返回
     * @Date 21:55 2021/6/23
     * @Param []
     **/
    public static <T> R<T> success(T data) {
        return success(data, null);
    }


    /**
     * @return com.kuangstudy.common.R
     * @Author xuke
     * @Description
     * @Date 22:03 2021/6/23
     * @Param [code 失败的状态, message 失败的原因]
     **/
    public static  <T> R<T> fail(Integer code, Object message) {
        R r = new R();
        r.setCode(code);
        r.setData(null);
        r.setMessage(message);
        return r;
    }

    /**
     * @return com.kuangstudy.common.R
     * @Author xuke
     * @Description
     * @Date 22:03 2021/6/23
     * @Param [code 失败的状态, message 失败的原因]
     **/
    public static  <T> R<T> fail(ResultEnum responseEnum) {
        R r = new R();
        r.setCode(responseEnum.getCode());
        r.setData(null);
        r.setMessage(responseEnum.getMessage());
        return r;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }
}

