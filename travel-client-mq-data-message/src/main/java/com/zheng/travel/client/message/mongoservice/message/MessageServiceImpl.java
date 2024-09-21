package com.zheng.travel.client.message.mongoservice.message;

import com.zheng.travel.client.message.mongorepository.MessageRepository;
import com.zheng.travel.client.message.mongo.MessageMo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessageServiceImpl implements IMessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public MessageMo saveMessage(MessageMo messageMo) {
      try {
          MessageMo messageMoDb = messageRepository.save(messageMo);
          return messageMoDb;
      }catch (Exception ex){
          ex.printStackTrace();
          return null;
      }
    }

}
