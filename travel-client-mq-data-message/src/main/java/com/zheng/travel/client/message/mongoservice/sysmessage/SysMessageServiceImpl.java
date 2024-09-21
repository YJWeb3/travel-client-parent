package com.zheng.travel.client.message.mongoservice.sysmessage;

import com.zheng.travel.client.message.mongo.SysMessageMo;
import com.zheng.travel.client.message.mongorepository.SysMessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SysMessageServiceImpl implements ISysMessageService {

    @Autowired
    private SysMessageRepository sysMessageRepository;


    @Override
    public SysMessageMo saveMessage(SysMessageMo sysMessageMo) {
        sysMessageMo.setId(null);
        SysMessageMo sysMessageMo1 = sysMessageRepository.save(sysMessageMo);
        return sysMessageMo1;
    }
}
