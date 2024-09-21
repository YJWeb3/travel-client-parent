package com.zheng.travel.client.controller.login;

import com.zheng.travel.client.bo.TravelUserBo;
import com.zheng.travel.client.idwork.IdWorkService;
import com.zheng.travel.client.localcache.EhCacheService;
import com.zheng.travel.client.redis.TravelRedisOperator;
import com.zheng.travel.client.service.user.IUserService;
import com.zheng.travel.client.service.weixin.WeixinData;
import com.zheng.travel.client.service.weixin.WeixinLoginService;
import com.zheng.travel.client.controller.APIBaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class PassportWeixinLoginController extends APIBaseController {

    @Autowired
    private WeixinLoginService weixinLoginService;
    @Autowired
    private EhCacheService ehCacheService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IdWorkService idWorkService;
    @Autowired
    private TravelRedisOperator redisOperator;

    /**
     * 获取微信授权信息
     *
     * @param weixinData
     * @return
     */
    @PostMapping(value = "/passport/weixin/phone")
    public WeixinData getSessionIdPhoneweixin(@RequestBody WeixinData weixinData) {
        WeixinData weixinOpendIdPhone = weixinLoginService.getWeixinOpendIdPhone(weixinData);
        return weixinOpendIdPhone;
    }

    /**
     * 小程序通过wx.login获取到的code去后台换取openid和session_key
     */
    @PostMapping("/passport/wxlogin/code")
    public TravelUserBo callback(@RequestBody WeixinData weixinData) {
        // 微信登录获取openid
        weixinData = weixinLoginService.getWeixinOpendId(weixinData);
        //把用户的openid注册到数据库中
        return userService.loginRegsiterWeixin(weixinData);
    }

}


