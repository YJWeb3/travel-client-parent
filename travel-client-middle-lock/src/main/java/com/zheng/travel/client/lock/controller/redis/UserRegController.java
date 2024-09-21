package com.zheng.travel.client.lock.controller.redis;

import com.zheng.travel.client.lock.common.R;
import com.zheng.travel.client.lock.common.StatusCode;
import com.zheng.travel.client.lock.service.redis.IUserRegService;
import com.zheng.travel.client.lock.vo.UserRegVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@Slf4j
@Api(tags = "Redis分布式锁--用户注册")
public class UserRegController {

    @Autowired
    private IUserRegService userRegService;


    @PostMapping("/user/reg/uuid")
    public String reg() {
        return System.nanoTime() + UUID.randomUUID().toString();
    }


    @ApiOperation("用户注册")
    @GetMapping("/user/reg")
    public R reguser(UserRegVo userRegVo) {
        if (StringUtils.isEmpty(userRegVo.getUserName())) {
            return new R(701, "请输入用户...");
        }
        if (StringUtils.isEmpty(userRegVo.getPassword())) {
            return new R(701, "请输入密码...");
        }

        // 进行用户注册
        R response = new R(StatusCode.Success);
        try {
            // 无锁版本
            //userRegService.regUserNoLock(userRegVo);

            // redis分布式锁
            //userRegService.regUserRedisLock(userRegVo);

            // zk的分布式锁
            //userRegService.regUserZkLock(userRegVo);

            // Redisson分布式锁
            userRegService.regUserRedissionLock(userRegVo);

        } catch (Exception ex) {
            response = new R(701, "注册用户失败");
        }

        return response;
    }

}
