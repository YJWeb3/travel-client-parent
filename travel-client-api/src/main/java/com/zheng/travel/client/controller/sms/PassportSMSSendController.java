package com.zheng.travel.client.controller.sms;

import com.zheng.travel.client.constant.TravelContants;
import com.zheng.travel.client.controller.APIBaseController;
import com.zheng.travel.client.result.ResultEnum;
import com.zheng.travel.client.result.ex.TravelValidationException;
import com.zheng.travel.client.service.sms.SmsService;
import com.zheng.travel.client.utils.fn.asserts.Vsserts;
import com.zheng.travel.client.utils.ip.IpUtils;
import com.zheng.travel.client.utils.valid.ValidatorUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "短信发送")
@RestController
@RequiredArgsConstructor
public class PassportSMSSendController extends APIBaseController {

    private final SmsService smsService;

    @ApiOperation("根据手机号码发送短信")
    @PostMapping("/passport/smssend/{phone}")
    public String sendPhoneCode(@PathVariable("phone") String phone, HttpServletRequest request) {
        Vsserts.isEmptyEx(phone, "请输入手机号码");
        Vsserts.isFalse(ValidatorUtil.isValidatorPhone(phone), "请输入正确的手机号码");
        // 1: 生成一个发送短信的随机数
        String code = (int) ((Math.random() * 9 + 1) * 100000) + "";
        // 2: 发送短信的接口
        boolean sendSMS = smsService.sendSMS(phone, code);
        if (sendSMS) {
            // 把生成的验证码放入到redis缓存中，用于后续的验证
            redisOperator.set(TravelContants.TRAVEL_CLIENT_LOGIN_SMS_CODE + phone, code);
            // 获取用户IP，防止用户在60s更换手机或者刷新应用重新发送
            String userIp = IpUtils.getIpAddr(request);
            redisOperator.setnx60s(TravelContants.TRAVEL_CLIENT_LOGIN_SMS_LIMIT + userIp, code);
            // 发送短信成功
            return "success";
        } else throw new TravelValidationException(ResultEnum.SMS_SEND_FAIL);
    }
}
