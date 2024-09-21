package com.zheng.travel.client.config.middle.listerner;

import com.zheng.travel.client.event.MongoEvent;
import com.zheng.travel.client.service.message.MongoDbMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class MongoDbMessageListerner implements SmartApplicationListener {

    @Autowired
    public MongoDbMessageService mongoDbMessageService;

    @Override
    public int getOrder() {
        return 1;
    }


    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if(event.getClass() == MongoEvent.class){
            MongoEvent mongoEvent = (MongoEvent)event;
            mongoDbMessageService.saveMongoMessage(mongoEvent);
            log.info("MongoEvent -----------> 保存订单消息成功!!!!");
        }
    }

    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
        return true;
    }
}

