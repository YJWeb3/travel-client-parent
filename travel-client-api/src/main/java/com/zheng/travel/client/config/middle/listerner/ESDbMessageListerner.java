package com.zheng.travel.client.config.middle.listerner;

import com.zheng.travel.client.event.EsEvent;
import com.zheng.travel.client.service.message.EsDbMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ESDbMessageListerner implements SmartApplicationListener {

    @Autowired
    public EsDbMessageService esDbMessageService;

    @Override
    public int getOrder() {
        return 1;
    }


    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if(event.getClass() == EsEvent.class){
            EsEvent esEvent = (EsEvent)event;
            esDbMessageService.saveESMessage(esEvent.getParameter());
            log.info("EsEvent---------保存订单消息成功!!!!");
        }
    }

    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
        return true;
    }
}

