package com.zheng.travel.client.result;

/**
 * @description: 统一返回的常量类
 * 对内修改开放，对外修改关闭---枚举
 */
public enum ResultEnum {

    SUCCESS(200,"成功!"),
    UNKNOWN_REASON(201, "未知错误"),
    SERVER_ERROR(201,  "服务器忙，请稍后在试"),
    EMPTY_ERROR(201,  "不允许为空"),
    ORDER_CREATE_FAIL(201, "订单下单失败"),

    SMS_SEND_FAIL(603, "短信发送失败!"),
    LOGIN_FAIL(609, "请登录"),
    SAME_LOGIN(610, "您已在别的地方登录了!"),

    USER_REG_USER_PASSWORD_CODE(401,"手机账号或密码有误"),
    USER_REG_USER_PASSWORD_CONFIRM(402,"你输入的密码有误"),
    ORDER_FAIL(601,"订单失败"),
    ORDER_MESSAGE_FAIL(602,"订单发送消息失败") ;

    private Integer code;
    private String message;

    ResultEnum(Integer code, String mesage){
        this.code = code;
        this.message =mesage;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
