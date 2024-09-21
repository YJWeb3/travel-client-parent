package com.zheng.travel.client.service.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zheng.travel.client.bo.TravelUserBo;
import com.zheng.travel.client.entity.TravelUser;
import com.zheng.travel.client.service.weixin.WeixinData;
import com.zheng.travel.client.vo.TravelUserVo;

public interface IUserService extends IService<TravelUser> {


    /**
     * 短信注册和登录
     * @param TravelUserVo
     * @return
     */
    TravelUserBo loginRegSms(TravelUserVo TravelUserVo);

    /**
     * 用于微信登录注册
     * @param weixinData
     * @return
     */
    TravelUserBo loginRegsiterWeixin(WeixinData weixinData);

    /**
     * 根据用户手机号码查询用户
     * @param phone
     * @return
     */
    TravelUser getByPhone(String phone);

    /**
     * 根据openid判断是否注册
     * @param openid
     * @return
     */
    TravelUser getByOpenId(String openid);
}
