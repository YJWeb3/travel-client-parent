package com.zheng.travel.client.service.weixin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zheng.travel.client.constant.TravelContants;
import com.zheng.travel.client.service.user.IUserService;
import com.zheng.travel.client.entity.TravelUser;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.spec.AlgorithmParameterSpec;

@Service
@Log4j2
public class WeixinLoginService implements TravelContants {

    @Autowired
    private IUserService userService;

    private static final String WXPATH = "https://api.weixin.qq.com/sns/jscode2session";
    private static final String APPID = "wx2130f87b8f96cff7";
    private static final String APPSCRET = "b92cbe561d9643fa2f8b4d1b6a7fea11";

    /**
     * wx.login 换取token的信息
     * 注：根据openid来做唯一性判断
     * @param weixinData
     * @return
     */
    public WeixinData getWeixinOpendId(WeixinData weixinData) {
        String code = weixinData.getCode();
        String path = WXPATH + "?appid="+APPID+"&secret="+APPSCRET+"&js_code="+code+"&grant_type=authorization_code";
        try {
            // 向微信服务器发送get请求获取加密了的内容
            HttpGet httpGet = new HttpGet(path);
            CloseableHttpClient httpclient = HttpClients.createDefault();
            CloseableHttpResponse execute = httpclient.execute(httpGet);
            String jsonStr = EntityUtils.toString(execute.getEntity());
            JSONObject jsonObject = JSONObject.parseObject(jsonStr);
            log.info("2：---->解析微信的信息是 code：{}", code);
            log.info("3：--------->解析微信的信息是：{}", JSON.toJSONString(jsonObject));
            String sessionkey = jsonObject.getString("session_key");
            String openid = String.valueOf(jsonObject.get("openid"));
            String unionid = String.valueOf(jsonObject.get("unionid"));
            weixinData.setOpenid(openid);
            weixinData.setUnionid(unionid);
            weixinData.setSessionkey(sessionkey);
            weixinData.setCode(code);
            return weixinData;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("微信小程序手机号码解密异常，信息如下: {}", e.getMessage());
            return null;
        }
    }

    /**
     * wx.getPhonenumber 换取用户授权的手机号码
     * 注：根据openid来做唯一性判断
     *
     * @param weixinData
     * @return
     */
    @Transactional
    public WeixinData getWeixinOpendIdPhone(WeixinData weixinData) {
        try {
            String sessionkey = weixinData.getSessionkey();
            // 解密
            String phoneNumber = "";
            if (StringUtils.isNotEmpty(sessionkey)) {
                try {
                    byte[] encrypData = Base64Utils.decodeFromString(weixinData.getEncrypted());
                    byte[] ivData = Base64Utils.decodeFromString(weixinData.getIv());
                    byte[] sessionKey = Base64Utils.decodeFromString(sessionkey);
                    AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivData);
                    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                    SecretKeySpec keySpec = new SecretKeySpec(sessionKey, "AES");
                    cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

                    String resultString = new String(cipher.doFinal(encrypData), StandardCharsets.UTF_8);
                    JSONObject object = JSONObject.parseObject(resultString);
                    log.info("3:================>{}", JSON.toJSONString(object));

                    phoneNumber = String.valueOf(DefaultUtils.defaultValue(object.get("phoneNumber"), ""));
                    log.info("4:==============>手机号码是：{},openid:{}", phoneNumber, weixinData.getOpenid());
                    weixinData.setPhonenumber(phoneNumber);
                    // 根据用户id更新手机号码
                    TravelUser travelUser = new TravelUser();
                    travelUser.setId(weixinData.getUserid());
                    travelUser.setPhone(phoneNumber);
                    userService.updateById(travelUser);

                }catch (Exception e){
                    log.info("手机解析失败...");
                }
            }
            // 返回用户信息
            return weixinData;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("微信小程序手机号码解密异常，信息如下: {}", e.getMessage());
            return null;
        }
    }
}
