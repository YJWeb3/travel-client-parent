package com.zheng.travel.client.event;

import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ApplicationContextEvent;

public class EsEvent extends ApplicationContextEvent {

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

    public void setMethodResult(Object methodResult) {
        this.methodResult = methodResult;
    }

    public void setParameter(Object[] parameter) {
        this.parameter = parameter;
    }

    /**
     * Create a new ContextStartedEvent.
     *
     * @param source the {@code ApplicationContext} that the event is raised for
     *               (must not be {@code null})
     */
    public EsEvent(ApplicationContext source) {
        super(source);
    }
}
