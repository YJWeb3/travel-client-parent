package com.zheng.travel.client.sharding.util;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @desc AES 加密工具类
 */
public class AESUtils {
    public static final String CODE = "utf-8";
    private static final String KEY_ALGORITHM = "AES";
    /*** 默认的加密算法 */
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

    /**
     * @param algorithm 加密算法
     * @param mode      密码器模式
     * @param key       加密秘钥
     * @return
     */
    public static Cipher initCipher(String algorithm, int mode, Key key) {
        Cipher cipher = null;
        try {
            // 创建密码器
            cipher = Cipher.getInstance(algorithm);
            // 初始化为某种模式的密码器ENCRYPT_MODE/DECRYPT_MODE/WRAP_MODE/UNWRAP_MODE/PUBLIC_KEY/PRIVATE_KEY
            cipher.init(mode, key);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        return cipher;
    }

    public static byte[] doFinal(Cipher cipher, byte[] content) {
        try {
            return cipher.doFinal(content);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * AES 加密操作
     *
     * @param content  待加密内容
     * @param password 加密密码
     * @return 返回Base64转码后的加密数据
     */
    public static String encrypt(String content, String password) {
        try {

            byte[] byteContent = content.getBytes(CODE);
            //初始化密码器
            Cipher cipher = initCipher(DEFAULT_CIPHER_ALGORITHM, Cipher.ENCRYPT_MODE, getSecretKey(password));
            // 加密
            byte[] result = cipher.doFinal(byteContent);
            //通过Base64转码返回
            return Base64.getEncoder().encodeToString(result);
        } catch (Exception ex) {
            Logger.getLogger(AESUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    /**
     * AES 解密操作
     *
     * @param content
     * @param password
     * @return
     */
    public static String decrypt(String content, String password) {

        try {
            //初始化密码器
            Cipher cipher = initCipher(DEFAULT_CIPHER_ALGORITHM, Cipher.DECRYPT_MODE, getSecretKey(password));
            // 加密
            byte[] result = cipher.doFinal(Base64.getDecoder().decode(content));
            return new String(result, CODE);
        } catch (Exception ex) {
            Logger.getLogger(AESUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * 生成加密秘钥
     *
     * @return
     */
    private static SecretKeySpec getSecretKey(final String password) {
        //返回生成指定算法密钥生成器的 KeyGenerator 对象
        KeyGenerator kg = null;
        try {
            kg = KeyGenerator.getInstance(KEY_ALGORITHM);
            //AES 要求密钥长度为 128
            kg.init(128, new SecureRandom(password.getBytes()));
            //生成一个密钥
            SecretKey secretKey = kg.generateKey();
            // 转换为AES专用密钥
            return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(AESUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static void main(String[] args) {
        String s = "123456";
        String password = "1234568abcdefg";
        System.out.println("加密前的明文:" + s);
        String s1 = AESUtils.encrypt(s, password);
        System.out.println("密文:" + s1);
        System.out.println("解密后的明文:" + AESUtils.decrypt(s1, password));
    }
}
