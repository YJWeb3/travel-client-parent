package com.zheng.travel.client.controller.pay.relation;


import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Base64;
import java.util.HashMap;
import java.util.UUID;

public class WeixinchatPayUtils {


    public static String getNonceStr(){
        return UUID.randomUUID().toString()
                .replaceAll("-", "")
                .substring(0, 32);
    }

    /**
     * 参考网站 https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_5_4.shtml
     * @param appId
     * @param prepay_id
     * @return
     * @throws IOException
     * @throws SignatureException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public static HashMap<String,Object> getTokenWeixin (String appId, String prepay_id) throws IOException, SignatureException, NoSuchAlgorithmException, InvalidKeyException {
        // 获取随机字符串
        String nonceStr = getNonceStr();
        // 获取微信小程序支付package
        String packagestr = "prepay_id=" + prepay_id;
        long timestamp = System.currentTimeMillis() / 1000;
        //签名，使用字段appId、timeStamp、nonceStr、package计算得出的签名值
        String message = buildMessageTwo(appId,timestamp,nonceStr,packagestr);
        //获取对应的签名
        String signature = sign(message.getBytes("utf-8"));
        // 组装返回
        HashMap<String,Object> map = new HashMap<>();
        map.put("timeStamp",String.valueOf(timestamp));
        map.put("nonceStr",nonceStr);
        map.put("package", packagestr);
        map.put("signType","RSA");
        map.put("paySign",signature);

        return map;
    }

    private static String buildMessageTwo(String appId, long timestamp, String nonceStr, String packag) {
        return appId + "\n"
                + timestamp + "\n"
                + nonceStr + "\n"
                + packag + "\n";
    }

    private static String sign(byte[] message) throws NoSuchAlgorithmException, SignatureException, IOException, InvalidKeyException {
        Signature sign = Signature.getInstance("SHA256withRSA"); //SHA256withRSA
        sign.initSign(TravelStaticParameter.privateKey);
        sign.update(message);
        return Base64.getEncoder().encodeToString(sign.sign());
    }


}
