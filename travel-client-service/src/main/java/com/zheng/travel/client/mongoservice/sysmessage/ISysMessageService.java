package com.zheng.travel.client.mongoservice.sysmessage;

import com.zheng.travel.client.mongo.SysMessageMo;
import com.zheng.travel.client.vo.SysMessageVo;
import org.springframework.data.domain.Page;

public interface ISysMessageService {
    /**
     * 保存消息
     * @param sysMessageMo
     * @return
     */
    SysMessageMo saveMessage(SysMessageMo sysMessageMo);


    /**
     * 分页查询
     * @return
     */
    Page<SysMessageMo> findMessagePage(SysMessageVo sysMessageVo);
}
