package com.zheng.travel.client.utils.fn.inter;

/**
 * 分支处理接口
 **/
@FunctionalInterface
public interface BooleanHandler {

    /**
     * 分支操作
     *
     * @param trueHandle  为true时要进行的操作
     * @param falseHandle 为false时要进行的操作
     * @return void
     **/
    void handler(HandlerFn trueHandle, HandlerFn falseHandle);
}