
package com.zheng.travel.client.service.message;

public interface ITravelMessageService {
    /**
     * 注册消息
     *
     * @param parameters
     */
    void saveRegMessage(Object[] parameters);


    /**
     * 下单消息
     */
    void saveOrderMessage(Object[] parameters);
}
