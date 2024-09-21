package com.zheng.travel.client.event;

import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ApplicationContextEvent;

public class MongoEvent extends ApplicationContextEvent {


    // 获取方法的参数
    private Object[] parameter;
    // 获取方法的返回值
    private Object methodResult;

    public Object[] getParameter() {
        return parameter;
    }

    public Object getMethodResult() {
        return methodResult;
    }

    public void setParameter(Object[] parameter) {
        this.parameter = parameter;
    }

    public void setMethodResult(Object methodResult) {
        this.methodResult = methodResult;
    }

    /**
     * Create a new ContextStartedEvent.
     *
     * @param source the {@code ApplicationContext} that the event is raised for
     *               (must not be {@code null})
     */
    public MongoEvent(ApplicationContext source) {
        super(source);
    }
}
