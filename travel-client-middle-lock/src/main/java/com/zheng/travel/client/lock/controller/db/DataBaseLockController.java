package com.zheng.travel.client.lock.controller.db;

import com.zheng.travel.client.lock.common.R;
import com.zheng.travel.client.lock.dto.UserAccountDto;
import com.zheng.travel.client.lock.service.db.TravelNoLockUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 基于数据库的乐观悲观锁
 **/
@RestController
@Api(tags = "数据库的乐观锁和悲观锁")
@Slf4j
public class DataBaseLockController {

    @Autowired
    private TravelNoLockUserService TRAVELNoLockUserService;

    /**
     * 无锁版本 - 基于数据库的体现
     *
     * @return
     */
    @ApiOperation("无锁版本-基于数据库的体现")
    @RequestMapping("/money/take")
    public R takeMoneynoLock(UserAccountDto userAccountDto) {
        // 判断当前用户和余额是否传递
        if (StringUtils.isEmpty(userAccountDto.getUserId())) {
            return new R(701, "提现用户不存在!!!");
        }
        if (StringUtils.isEmpty(userAccountDto.getAmount())) {
            return new R(701, "提现金额不能为空!!!");
        }

        R response = new R(200, "success");
        try {
            // 无锁版本
            //TRAVELNoLockUserService.takeMoney(userAccountDto);
            // 乐观锁
            //TRAVELNoLockUserService.takeMoneyLock(userAccountDto);
            // 悲观锁
            TRAVELNoLockUserService.takeMoneyPessLock(userAccountDto);

        } catch (Exception ex) {
            response = new R(701, "提现失败,失败原因: " +ex.getMessage());
        }
        return response;
    }


}













































