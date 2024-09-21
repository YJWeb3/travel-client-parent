package com.zheng.travel.client.mongoservice.sysmessage;

import com.zheng.travel.client.mongo.SysMessageMo;
import com.zheng.travel.client.mongorepository.SysMessageRepository;
import com.zheng.travel.client.vo.SysMessageVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SysMessageServiceImpl implements ISysMessageService {

    @Autowired
    private SysMessageRepository sysMessageRepository;


    @Override
    public SysMessageMo saveMessage(SysMessageMo sysMessageMo) {
        SysMessageMo sysMessageMo1 = sysMessageRepository.save(sysMessageMo);
        return sysMessageMo1;
    }


    @Override
    public Page<SysMessageMo> findMessagePage(SysMessageVo sysMessageVo) {
        if (sysMessageVo.getPageNo() <= 0) {
            sysMessageVo.setPageNo(0);
        }
        if (sysMessageVo.getPageNo() > 0) {
            sysMessageVo.setPageNo(sysMessageVo.getPageNo() - 1);
        }
        // 1: 设置分页和排序 这个是mongodb的属性名
        Sort sort = Sort.by(Sort.Direction.DESC, "ctime");
        Pageable pageable = PageRequest.of(sysMessageVo.getPageNo(), sysMessageVo.getPageSize(), sort);
        //2:查询返条件设置
        SysMessageMo messageMo = new SysMessageMo();
        // 4: 设置查询条件和规则
        Example example = Example.of(messageMo);
        // 5：查询分页返回
        Page<SysMessageMo> messageMoPage = sysMessageRepository.findAll(example, pageable);
        return messageMoPage;
    }
}
