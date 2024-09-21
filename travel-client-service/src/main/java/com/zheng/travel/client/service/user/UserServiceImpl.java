package com.zheng.travel.client.service.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zheng.travel.client.anno.log.TravelMQMessage;
import com.zheng.travel.client.anno.log.TravelMessage;
import com.zheng.travel.client.bo.TravelUserBo;
import com.zheng.travel.client.constant.TravelContants;
import com.zheng.travel.client.event.MongoEvent;
import com.zheng.travel.client.idwork.IdWorkService;
import com.zheng.travel.client.redis.TravelRedisOperator;
import com.zheng.travel.client.result.ex.TravelBusinessException;
import com.zheng.travel.client.utils.FastJsonUtil;
import com.zheng.travel.client.utils.fn.asserts.Vsserts;
import com.zheng.travel.client.entity.TravelUser;
import com.zheng.travel.client.localcache.EhCacheService;
import com.zheng.travel.client.mapper.user.UserMapper;
import com.zheng.travel.client.service.weixin.WeixinData;
import com.zheng.travel.client.vo.TravelUserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, TravelUser> implements IUserService, TravelContants {

    @Autowired
    private EhCacheService ehCacheService;
    @Autowired
    private IdWorkService idWorkService;
    @Autowired
    private TravelRedisOperator redisOperator;


    @Override
    @Transactional
    @TravelMessage(eventClasses = MongoEvent.class)
    public TravelUserBo loginRegSms(TravelUserVo TravelUser) {
        // 1: 接受用户提交用户手机和短信
        String inputPhone = TravelUser.getPhone();
        String inputSmsCode = TravelUser.getSmscode();
        // 2: 获取redis缓存中的存储短信
        String cacheSmsCode = redisOperator.get(TRAVEL_CLIENT_LOGIN_SMS_CODE + inputPhone);
        Vsserts.isEmptyEx(cacheSmsCode, "短信已经失效");
        // 3: 对比短信码是否正确
        if (!cacheSmsCode.equalsIgnoreCase(inputSmsCode)) {
            throw new TravelBusinessException(602, "你输入短信证码有误!");
        }
        // 4：开始进行业务的处理
        TravelUser dbUser = this.getByPhone(inputPhone);
        // 如果dbUser，说明没有注册
        if (Vsserts.isNull(dbUser)) {
            // 开始注册
            dbUser = new TravelUser();
            dbUser.setPhone(inputPhone);
            dbUser.setIdcode(idWorkService.nextShort());
            dbUser.setNickname("小伴");
            dbUser.setPassword("");
            dbUser.setAvatar("http://122.152.205.72:88/group1/M00/00/05/CpoxxF6ZUySASMbOAABBAXhjY0Y649.png");
            dbUser.setSex(2);
            dbUser.setCountry("中国");
            dbUser.setProvince("");
            dbUser.setCity("");
            dbUser.setIsdelete(0);
            dbUser.setDistrict("");
            dbUser.setDescription("Ta什么都没有留下...");
            dbUser.setBgImg("biimg.jpg");
            dbUser.setStatus(1);
            this.saveOrUpdate(dbUser);

            // 回填信息 用于消息
            TravelUser.setNickname(dbUser.getNickname());
            TravelUser.setAvatar(dbUser.getAvatar());
            TravelUser.setId(dbUser.getId() + "");
        }

        // 5: 存在就返回
        TravelUserBo travelUserBo = new TravelUserBo();
        BeanUtils.copyProperties(dbUser, travelUserBo);
        // 6: 生成一个uuid代表token
        String tokenUuid = UUID.randomUUID().toString();
        travelUserBo.setToken(tokenUuid);
        // 7 : 登录成功以后，把用户信息放入到缓存中，只要是为了后续业务的需要，
        redisOperator.set(TRAVEL_CLIENT_LOGIN_USER_INFO + travelUserBo.getId(), FastJsonUtil.toJSON(dbUser));
        // token放入缓存中去，是给同一个用户在不同的端登录。前面登录会自动全部拦截下线。只有最后一次登录的才有效。
        redisOperator.set(TRAVEL_CLIENT_LOGIN_USER_TOKEN + travelUserBo.getId(), tokenUuid);
        // 8：而且短信码一定注册成功其实也没有任何用处，会占用redis内存空间，所有删除掉
        redisOperator.del(TRAVEL_CLIENT_LOGIN_SMS_CODE + inputPhone);

        // 本地缓存
        ehCacheService.setCache(TRAVEL_CLIENT_LOGIN_USER_INFO + travelUserBo.getId(), dbUser);
        ehCacheService.setCache(TRAVEL_CLIENT_LOGIN_USER_TOKEN + travelUserBo.getId(), tokenUuid);


        return travelUserBo;
    }


    @Override
    @Transactional
    @TravelMessage(eventClasses = MongoEvent.class)
    @TravelMQMessage(routingkey = "user.mongo.message")
    public TravelUserBo loginRegsiterWeixin(WeixinData weixinData){
        // 4：开始进行业务的处理
        TravelUser dbUser = this.getByOpenId(weixinData.getOpenid());
        // 如果dbUser，说明没有注册
        if (Vsserts.isNull(dbUser)) {
            // 开始注册
            dbUser = new TravelUser();
            dbUser.setOpenid(weixinData.getOpenid());
            dbUser.setUnionid(weixinData.getUnionid());
            dbUser.setPhone(weixinData.getPhonenumber());
            dbUser.setIdcode(idWorkService.nextShort());
            dbUser.setNickname("小伴");
            dbUser.setPassword("");
            dbUser.setAvatar("http://122.152.205.72:88/group1/M00/00/05/CpoxxF6ZUySASMbOAABBAXhjY0Y649.png");
            dbUser.setSex(2);
            dbUser.setCountry("中国");
            dbUser.setProvince("");
            dbUser.setCity("");
            dbUser.setIsdelete(0);
            dbUser.setDistrict("");
            dbUser.setDescription("Ta什么都没有留下...");
            dbUser.setBgImg("http://39.105.38.113:9000/abc/2110058DPP1ZPKKP/1633406083.jpg");
            dbUser.setStatus(1);
            this.saveOrUpdate(dbUser);

            // 回填信息 用于消息
            weixinData.setNickName(dbUser.getNickname());
            weixinData.setAvatarUrl(dbUser.getAvatar());
            weixinData.setUserid(dbUser.getId());
        }

        // 5: 存在就返回
        TravelUserBo travelUserBo = new TravelUserBo();
        BeanUtils.copyProperties(dbUser, travelUserBo);
        // 6: 生成一个uuid代表token
        String tokenUuid = UUID.randomUUID().toString();
        travelUserBo.setToken(tokenUuid);
        travelUserBo.setSessionkey(weixinData.getSessionkey());
        travelUserBo.setOpenid(weixinData.getOpenid());
        travelUserBo.setUnionid(weixinData.getUnionid());
        // 7 : 登录成功以后，把用户信息放入到缓存中，只要是为了后续业务的需要，
        redisOperator.set(TRAVEL_CLIENT_LOGIN_USER_INFO + travelUserBo.getId(), FastJsonUtil.toJSON(dbUser));
        // token放入缓存中去，是给同一个用户在不同的端登录。前面登录会自动全部拦截下线。只有最后一次登录的才有效。
        redisOperator.set(TRAVEL_CLIENT_LOGIN_USER_TOKEN + travelUserBo.getId(), tokenUuid);
        // 本地缓存
        ehCacheService.setCache(TRAVEL_CLIENT_LOGIN_USER_INFO + travelUserBo.getId(), dbUser);
        ehCacheService.setCache(TRAVEL_CLIENT_LOGIN_USER_TOKEN + travelUserBo.getId(), tokenUuid);



        return travelUserBo;
    }


    /**
     * 根据用户手机号码查询用户
     *
     * @param phone
     * @return
     */
    @Override
    public TravelUser getByPhone(String phone) {
        LambdaQueryWrapper<TravelUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(TravelUser::getPhone, phone);
        return this.getOne(lambdaQueryWrapper);
    }


    /**
     * 根据openid判断是否注册
     *
     * @param openid
     * @return
     */
    @Override
    public TravelUser getByOpenId(String openid) {
        LambdaQueryWrapper<TravelUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(TravelUser::getOpenid, openid);
        return this.getOne(lambdaQueryWrapper);
    }
}
