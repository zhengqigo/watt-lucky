package org.fuelteam.watt.lucky.base;

import org.apache.commons.lang3.BooleanUtils;

/**
 * 逻辑运算工具类
 * @see BooleanUtils
 */
public class BooleanUtil {

    /**
     * 忽略大小写的"true"返回true，其它返回false
     */
    public static boolean toBoolean(String str) {
        return Boolean.parseBoolean(str);
    }

    /**
     * 忽略大小写的"true"返回true，为空时返回null，其它返回false
     */
    public static Boolean toBooleanObject(String str) {
        return str != null ? Boolean.valueOf(str) : null;
    }

    /**
     * 忽略大小写的"true"返回true，为空时返回defaultValue，其它返回false
     */
    public static Boolean toBooleanObject(String str, Boolean defaultValue) {
        return str != null ? Boolean.valueOf(str) : defaultValue;
    }

    /**
     * 支持true/false, on/off, y/n, yes/no转换，str为空或无法分析时返回null
     */
    public static Boolean parseGeneralString(String str) {
        return BooleanUtils.toBooleanObject(str);
    }

    /**
     * 支持true/false，on/off，y/n，yes/no转换，str为空或无法分析时返回defaultValue
     */
    public static Boolean parseGeneralString(String str, Boolean defaultValue) {
        return BooleanUtils.toBooleanDefaultIfNull(BooleanUtils.toBooleanObject(str), defaultValue);
    }

    public static boolean negate(final boolean bool) {
        return !bool;
    }

    public static Boolean negate(final Boolean bool) {
        return BooleanUtils.negate(bool);
    }

    /**
     * 多个值的and
     */
    public static boolean and(final boolean... array) {
        return BooleanUtils.and(array);
    }

    /**
     * 多个值的or
     */
    public static boolean or(final boolean... array) {
        return BooleanUtils.or(array);
    }
}