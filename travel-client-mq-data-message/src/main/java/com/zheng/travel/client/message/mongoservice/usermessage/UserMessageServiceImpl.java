package com.zheng.travel.client.message.mongoservice.usermessage;

import com.zheng.travel.client.message.mongo.UserMessageMo;
import com.zheng.travel.client.message.mongorepository.UserMessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserMessageServiceImpl implements IUserMessageService {

    @Autowired
    private UserMessageRepository userMessageRepository;


    @Override
    public UserMessageMo saveMessage(UserMessageMo userMessageMo) {
        userMessageMo.setId(null);
        UserMessageMo userMessageMo1 = userMessageRepository.save(userMessageMo);
        return userMessageMo1;
    }
}
