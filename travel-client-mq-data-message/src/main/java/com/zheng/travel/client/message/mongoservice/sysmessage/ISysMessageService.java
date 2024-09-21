package com.zheng.travel.client.message.mongoservice.sysmessage;

import com.zheng.travel.client.message.mongo.SysMessageMo;

public interface ISysMessageService {
    /**
     * 保存消息
     * @param sysMessageMo
     * @return
     */
    SysMessageMo saveMessage(SysMessageMo sysMessageMo);

}
