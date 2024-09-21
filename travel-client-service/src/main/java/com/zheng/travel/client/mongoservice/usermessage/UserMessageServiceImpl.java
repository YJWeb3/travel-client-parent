package com.zheng.travel.client.mongoservice.usermessage;

import com.zheng.travel.client.mongo.UserMessageMo;
import com.zheng.travel.client.mongorepository.UserMessageRepository;
import com.zheng.travel.client.vo.UserMessageVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserMessageServiceImpl implements IUserMessageService {

    @Autowired
    private UserMessageRepository userMessageRepository;


    @Override
    public UserMessageMo saveMessage(UserMessageMo userMessageMo) {
        UserMessageMo userMessageMo1 = userMessageRepository.save(userMessageMo);
        return userMessageMo1;
    }


    @Override
    public Page<UserMessageMo> findMessagePage(UserMessageVo userMessageVo) {
        if (userMessageVo.getPageNo() <= 0) {
            userMessageVo.setPageNo(0);
        }
        if (userMessageVo.getPageNo() > 0) {
            userMessageVo.setPageNo(userMessageVo.getPageNo() - 1);
        }
        // 1: 设置分页和排序 这个是mongodb的属性名
        Sort sort = Sort.by(Sort.Direction.DESC, "ctime");
        Pageable pageable = PageRequest.of(userMessageVo.getPageNo(), userMessageVo.getPageSize(), sort);
        //2:查询返条件设置
        UserMessageMo messageMo = new UserMessageMo();
        messageMo.setUserid(userMessageVo.getUserId());
        // 4: 设置查询条件和规则
        Example example = Example.of(messageMo);
        // 5：查询分页返回
        Page<UserMessageMo> messageMoPage = userMessageRepository.findAll(example, pageable);
        return messageMoPage;
    }
}
