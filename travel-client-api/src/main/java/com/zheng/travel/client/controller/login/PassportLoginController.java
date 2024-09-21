package com.zheng.travel.client.controller.login;

import com.zheng.travel.client.bo.TravelUserBo;
import com.zheng.travel.client.idwork.IdWorkService;
import com.zheng.travel.client.localcache.EhCacheService;
import com.zheng.travel.client.result.ResultEnum;
import com.zheng.travel.client.result.ex.TravelBusinessException;
import com.zheng.travel.client.service.message.MongoDbMessageService;
import com.zheng.travel.client.service.user.IUserService;
import com.zheng.travel.client.utils.FastJsonUtil;
import com.zheng.travel.client.utils.aes.DesUtils;
import com.zheng.travel.client.utils.fn.asserts.Vsserts;
import com.zheng.travel.client.utils.pwd.MD5Util;
import com.zheng.travel.client.utils.valid.ValidatorUtil;
import com.zheng.travel.client.controller.APIBaseController;
import com.zheng.travel.client.entity.TravelUser;
import com.zheng.travel.client.vo.TravelLogoutVo;
import com.zheng.travel.client.vo.TravelUserPwdVo;
import com.zheng.travel.client.vo.TravelUserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@Api(tags = "登录管理")
@RestController
@RequiredArgsConstructor
public class PassportLoginController extends APIBaseController {

    private final IUserService userService;
    private final IdWorkService idWorkService;
    private final EhCacheService ehCacheService;
    private final MongoDbMessageService TravelMessasgeService;


    @ApiOperation("登出方法")
    @PostMapping("/passport/logout")
    public String toLoginReg(@RequestBody TravelLogoutVo TravelLogoutVo) {
        // 删除缓存中中用户信息
        redisOperator.del(TRAVEL_CLIENT_LOGIN_USER_INFO + TravelLogoutVo.getUserId());
        // 删除缓存中中token
        redisOperator.del(TRAVEL_CLIENT_LOGIN_USER_TOKEN + TravelLogoutVo.getUserId());
        // 本地缓存
        ehCacheService.removeCache(TRAVEL_CLIENT_LOGIN_USER_INFO + TravelLogoutVo.getUserId());
        ehCacheService.removeCache(TRAVEL_CLIENT_LOGIN_USER_TOKEN + TravelLogoutVo.getUserId());
        return "success";
    }


    @ApiOperation("短信登录方法")
    @PostMapping("/passport/login")
    public TravelUserBo toLoginReg(@RequestBody @Valid TravelUserVo TravelUser) {
        return userService.loginRegSms(TravelUser);
    }



    @ApiOperation("密码登录方法")
    @PostMapping("/passport/loginpwd")
    public TravelUserBo toLoginPwd(@RequestBody @Valid TravelUserPwdVo TravelUser) {
        // 1: 接受用户提交用户手机和短信
        String inputPhone = TravelUser.getPhone();
        String inputPassword = TravelUser.getPassword();
        inputPhone = DesUtils.decrypt(inputPhone);
        inputPassword = DesUtils.decrypt(inputPassword);

        Vsserts.isFalse(ValidatorUtil.isValidatorPhone(inputPhone),"请输入正确的手机号码");
        // 4：开始进行业务的处理
        TravelUser dbUser = userService.getByPhone(inputPhone);
        // 如果dbUser，说明没有注册
        if (Vsserts.isNotNull(dbUser)) {
            // 查询数据的密码
            String dbPassword = dbUser.getPassword();
            // 拿用户输入的密码和数据库的密码进行比较。如果不相等直接抛出异常
            inputPassword = MD5Util.md5slat(inputPassword);
            if(!inputPassword .equalsIgnoreCase(dbPassword)){
                throw new TravelBusinessException(ResultEnum.USER_REG_USER_PASSWORD_CONFIRM);
            }
            // 5: 存在就返回
            TravelUserBo TravelUserBo = new TravelUserBo();
            BeanUtils.copyProperties(dbUser, TravelUserBo);
            // 6: 生成一个uuid代表token
            String tokenUuid = UUID.randomUUID().toString();
            TravelUserBo.setToken(tokenUuid);


            // 7 : 登录成功以后，把用户信息放入到缓存中，只要是为了后续业务的需要，
            redisOperator.set(TRAVEL_CLIENT_LOGIN_USER_INFO + TravelUserBo.getId(), FastJsonUtil.toJSON(dbUser));
            // token放入缓存中去，是给同一个用户在不同的端登录。前面登录会自动全部拦截下线。只有最后一次登录的才有效。
            redisOperator.set(TRAVEL_CLIENT_LOGIN_USER_TOKEN + TravelUserBo.getId(), tokenUuid);
            // 8：而且短信码一定注册成功其实也没有任何用处，会占用redis内存空间，所有删除掉
            redisOperator.del(TRAVEL_CLIENT_LOGIN_SMS_CODE + inputPhone);


            // 本地缓存
            ehCacheService.setCache(TRAVEL_CLIENT_LOGIN_USER_INFO + TravelUserBo.getId(),dbUser);
            ehCacheService.setCache(TRAVEL_CLIENT_LOGIN_USER_TOKEN + TravelUserBo.getId(), tokenUuid);

            return TravelUserBo;
        }else{
            // 如果根据手机号码找不到用户，说明手机号码输入有误
            throw new TravelBusinessException(ResultEnum.USER_REG_USER_PASSWORD_CODE);
        }
    }
}
