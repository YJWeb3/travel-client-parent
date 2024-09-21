package com.zheng.travel.client.service.sms;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.zheng.travel.client.result.ex.TravelBusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SmsService {

    public boolean sendSMS(String phone, String code) {
        try {
            Client client = SMSUtils.createClient("LTAI5tJVn7bGRRn8wzZ2iuS7", "6LHqoBOyweUnAQIQSO5zv8IoE9kYj9");
            SendSmsRequest sendSmsRequest = new SendSmsRequest()
                    .setPhoneNumbers(phone)
                    .setSignName("同学你好网络科技")
                    .setTemplateCode("SMS_235792419")
                    .setTemplateParam("{\"code\":\"" + code + "\"}");
            // 复制代码运行请自行打印 API 的返回值
            SendSmsResponse sendSmsResponse = client.sendSms(sendSmsRequest);
            return sendSmsResponse.getBody().getCode().equalsIgnoreCase("ok");
        } catch (Exception ex) {
            throw new TravelBusinessException(601, "短信发送失败!!");
        }

    }
}
