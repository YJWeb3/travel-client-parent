package com.zheng.travel.client.controller.pay.relation;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 定义微信支付参数配置类
 **/
public class TravelStaticParameter {
    /**
     * 微信商户号
     */
    public static String mchId;

    /**
     * 商户在微信公众平台申请服务号对应的APPID
     */
    public static String appId;

    /**
     * 回调报文解密V3密钥key
     */
    public static String v3Key;

    /**
     * 微信获取平台证书列表地址
     */
    public static String certificatesUrl;

    /**
     * 微信APP下单URL
     */
    public static String unifiedOrderUrl;

    /**
     * 商户订单号查询URL
     */
    public static final String TRANSACTIONS_OUT_TRADE_NO_URL = "https://api.mch.weixin.qq.com/v3/pay/transactions/out-trade-no/%s?mchid=%s";

    /**
     * 异步接收微信支付结果通知的回调地址
     */
    public static String notifyUrl;

    /**
     * 微信证书私钥
     */
    public static PrivateKey privateKey;

    /**
     * 微信商家api序列号
     */
    public static String mchSerialNo;

    // 定义全局容器 保存微信平台证书公钥
    public static Map<String, X509Certificate> certificateMap = new ConcurrentHashMap<>();

    public static void certificateMap(String serialNo) {
    }
}
