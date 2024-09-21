package com.zheng.travel.client.event;

import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ApplicationContextEvent;


public class DBEvent extends ApplicationContextEvent {

    private Object[] parameter;

    public Object[] getParameter() {
        return parameter;
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
    public DBEvent(ApplicationContext source) {
        super(source);
    }
}
