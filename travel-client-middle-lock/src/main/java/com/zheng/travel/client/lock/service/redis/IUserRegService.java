package com.zheng.travel.client.lock.service.redis;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zheng.travel.client.lock.model.UserReg;
import com.zheng.travel.client.lock.vo.UserRegVo;

public interface IUserRegService extends IService<UserReg> {
    // 无锁
    void regUserNoLock(UserRegVo userRegVo);

    // redis的分布式锁
    void regUserRedisLock(UserRegVo userRegVo);

    // zk的分布式锁
    void regUserZkLock(UserRegVo userRegVo) throws Exception;

    // 使用redsisson完成分布式锁
    void regUserRedissionLock(UserRegVo userRegVo);
}
