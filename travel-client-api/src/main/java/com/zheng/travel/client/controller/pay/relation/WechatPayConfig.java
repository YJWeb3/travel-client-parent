package com.zheng.travel.client.controller.pay.relation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class WechatPayConfig implements CommandLineRunner {
    /**
     * 公众号appid
     */
    @Value("${wechat.appId}")
    private String wechatAppId;

    /**
     * 商户号id
     */
    @Value("${wechat.mchId}")
    private String wechatMchId;

    /**
     * 商户序列号
     */
    @Value("${wechat.mchSerialNo}")
    private String mchSerialNo;
    /**
     * 支付key
     */
    @Value("${wechat.v3Key}")
    private String wechatV3Key;

    /**
     * 微信支付回调url
     */
    @Value("${wechat.callback}")
    private String payCallbackUrl;

    /**
     * 统一下单url
     */

    @Value("${wechat.unifiedOrder.url}")
    private String wechatUnifiedOrderUrl;

    /**
     * 平台证书列表地址
     */
    @Value("${wechat.certificates.url}")
    private String wechatCertificatesUrl;

    /**
     * 商户私钥路径
     */
    @Value("${wechat.key.path}")
    private String wechatKeyPath;

    @Override
    public void run(String... args) {
        //微信支付
        TravelStaticParameter.mchId = wechatMchId;
        TravelStaticParameter.appId = wechatAppId;
        TravelStaticParameter.v3Key = wechatV3Key;
        TravelStaticParameter.certificatesUrl = wechatCertificatesUrl;
        TravelStaticParameter.unifiedOrderUrl = wechatUnifiedOrderUrl;
        TravelStaticParameter.notifyUrl = payCallbackUrl;
        try {
            //加载商户私钥
            TravelStaticParameter.privateKey = WechatPayUtils.getPrivateKey(wechatKeyPath);
            TravelStaticParameter.mchSerialNo = mchSerialNo;
            //获取平台证书
            TravelStaticParameter.certificateMap = WechatPayUtils.refreshCertificate();
        } catch (Exception e) {
            log.error("加载微信支付文件异常", e);
        }
    }
}
