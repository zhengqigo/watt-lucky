package org.fuelteam.watt.lucky.security;

import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.fuelteam.watt.lucky.exception.ExceptionUtil;
import org.fuelteam.watt.lucky.number.RandomUtil;
import org.fuelteam.watt.lucky.text.Charsets;

public class CryptoUtil {

    private static final String AES_ALG = "AES";
    private static final String AES_CBC_ALG = "AES/CBC/PKCS5Padding";
    private static final String HMACSHA1_ALG = "HmacSHA1";

    private static final int DEFAULT_HMACSHA1_KEYSIZE = 160; // RFC2401
    private static final int DEFAULT_AES_KEYSIZE = 128;
    private static final int DEFAULT_IVSIZE = 16;

    private static SecureRandom random = RandomUtil.secureRandom();

    // 使用HMAC-SHA1进行消息签名, 返回长度为20字节的字节数组
    public static byte[] hmacSha1(byte[] input, byte[] key) {
        try {
            SecretKey secretKey = new SecretKeySpec(key, HMACSHA1_ALG);
            Mac mac = Mac.getInstance(HMACSHA1_ALG);
            mac.init(secretKey);
            return mac.doFinal(input);
        } catch (GeneralSecurityException e) {
            throw ExceptionUtil.unchecked(e);
        }
    }

    // 校验HMAC-SHA1签名是否正确
    public static boolean isMacValid(byte[] expected, byte[] input, byte[] key) {
        byte[] actual = hmacSha1(input, key);
        return Arrays.equals(expected, actual);
    }

    // 生成HMAC-SHA1密钥, 返回长度为160位(20字节)的字节数组, HMAC-SHA1算法对密钥无特殊要求, RFC2401建议最少长度为160位(20字节)
    public static byte[] generateHmacSha1Key() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(HMACSHA1_ALG);
            keyGenerator.init(DEFAULT_HMACSHA1_KEYSIZE);
            SecretKey secretKey = keyGenerator.generateKey();
            return secretKey.getEncoded();
        } catch (GeneralSecurityException e) {
            throw ExceptionUtil.unchecked(e);
        }
    }

    // 使用AES加密原始字符串
    public static byte[] aesEncrypt(byte[] input, byte[] key) {
        return aes(input, key, Cipher.ENCRYPT_MODE);
    }

    // 使用AES加密原始字符串
    public static byte[] aesEncrypt(byte[] input, byte[] key, byte[] iv) {
        return aes(input, key, iv, Cipher.ENCRYPT_MODE);
    }

    // 使用AES解密字符串返回原始字符串
    public static String aesDecrypt(byte[] input, byte[] key) {
        byte[] decryptResult = aes(input, key, Cipher.DECRYPT_MODE);
        return new String(decryptResult, Charsets.UTF_8);
    }

    // 使用AES解密字符串返回原始字符串
    public static String aesDecrypt(byte[] input, byte[] key, byte[] iv) {
        byte[] decryptResult = aes(input, key, iv, Cipher.DECRYPT_MODE);
        return new String(decryptResult, Charsets.UTF_8);
    }

    // 使用AES加密或解密无编码的原始字节数组返回无编码的字节数组结果, Cipher.ENCRYPT_MODE | Cipher.DECRYPT_MODE
    private static byte[] aes(byte[] input, byte[] key, int mode) {
        try {
            SecretKey secretKey = new SecretKeySpec(key, AES_ALG);
            Cipher cipher = Cipher.getInstance(AES_ALG);
            cipher.init(mode, secretKey);
            return cipher.doFinal(input);
        } catch (GeneralSecurityException e) {
            throw ExceptionUtil.unchecked(e);
        }
    }

    // 使用AES加密或解密无编码的原始字节数组返回无编码的字节数组结果, Cipher.ENCRYPT_MODE | Cipher.DECRYPT_MODE
    private static byte[] aes(byte[] input, byte[] key, byte[] iv, int mode) {
        try {
            SecretKey secretKey = new SecretKeySpec(key, AES_ALG);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            Cipher cipher = Cipher.getInstance(AES_CBC_ALG);
            cipher.init(mode, secretKey, ivSpec);
            return cipher.doFinal(input);
        } catch (GeneralSecurityException e) {
            throw ExceptionUtil.unchecked(e);
        }
    }

    // 生成AES密钥返回字节数组, 默认长度为128位(16字节)
    public static byte[] generateAesKey() {
        return generateAesKey(DEFAULT_AES_KEYSIZE);
    }

    // 生成AES密钥，可选长度为128|192|256位
    public static byte[] generateAesKey(int keysize) {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(AES_ALG);
            keyGenerator.init(keysize);
            SecretKey secretKey = keyGenerator.generateKey();
            return secretKey.getEncoded();
        } catch (GeneralSecurityException e) {
            throw ExceptionUtil.unchecked(e);
        }
    }

    // 生成随机向量, 默认大小为cipher.getBlockSize(), 16字节
    public static byte[] generateIV() {
        byte[] bytes = new byte[DEFAULT_IVSIZE];
        random.nextBytes(bytes);
        return bytes;
    }
}