package org.fuelteam.watt.lucky.utils;

import org.apache.commons.lang3.BooleanUtils;

public class BooleanUtil {

    public static boolean toBoolean(String str) {
        return Boolean.parseBoolean(str);
    }

    public static Boolean toBooleanObject(String str) {
        return str != null ? Boolean.valueOf(str) : null;
    }

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

    public static boolean and(final boolean... array) {
        return BooleanUtils.and(array);
    }

    public static boolean or(final boolean... array) {
        return BooleanUtils.or(array);
    }
}