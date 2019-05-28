package org.fuelteam.watt.lucky.text;

import com.google.common.io.BaseEncoding;

public class EncodeUtil {

    // 将byte[]编码为String，默认为ABCDEF大写字母
    public static String encodeHex(byte[] input) {
        return BaseEncoding.base16().encode(input);
    }

    // 将String解码为byte[]，字符不合法抛IllegalArgumentException
    public static byte[] decodeHex(CharSequence input) {
        return BaseEncoding.base16().decode(input);
    }

    // Base64编码
    public static String encodeBase64(byte[] input) {
        return BaseEncoding.base64().encode(input);
    }

    // Base64解码，字符不合法抛IllegalArgumentException
    public static byte[] decodeBase64(CharSequence input) {
        return BaseEncoding.base64().decode(input);
    }

    // 将Base64中的URL非法字符'+'和'/'转为'-'和'_'，见RFC3548
    public static String encodeBase64UrlSafe(byte[] input) {
        return BaseEncoding.base64Url().encode(input);
    }

    //将Base64中的URL非法字符'+'和'/'转为'-'和'_'，见RFC3548，字符不合法抛IllegalArgumentException
    public static byte[] decodeBase64UrlSafe(CharSequence input) {
        return BaseEncoding.base64Url().decode(input);
    }
}