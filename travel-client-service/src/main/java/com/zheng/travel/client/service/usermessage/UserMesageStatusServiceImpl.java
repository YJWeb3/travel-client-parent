
package com.zheng.travel.client.service.usermessage;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zheng.travel.client.entity.UserMesageStatus;
import com.zheng.travel.client.mapper.message.UserMesageStatusMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class UserMesageStatusServiceImpl extends ServiceImpl<UserMesageStatusMapper, UserMesageStatus> implements IUserMesageStatusService {
}
