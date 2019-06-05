package org.fuelteam.watt.lucky.number;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.fuelteam.watt.lucky.annotation.NotNull;
import org.fuelteam.watt.lucky.annotation.Nullable;

import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;

public class NumberUtil {

    private static final double DEFAULT_DOUBLE_EPSILON = 0.00001d;

    public static boolean equalsWithin(double d1, double d2) {
        return Math.abs(d1 - d2) < DEFAULT_DOUBLE_EPSILON;
    }

    public static boolean equalsWithin(double d1, double d2, double epsilon) {
        return Math.abs(d1 - d2) < epsilon;
    }

    public static byte[] toBytes(int value) {
        return Ints.toByteArray(value);
    }

    public static byte[] toBytes(long value) {
        return Longs.toByteArray(value);
    }

    public static byte[] toBytes(double val) {
        return toBytes(Double.doubleToRawLongBits(val));
    }

    public static int toInt(byte[] bytes) {
        return Ints.fromByteArray(bytes);
    }

    public static long toLong(byte[] bytes) {
        return Longs.fromByteArray(bytes);
    }

    public static double toDouble(byte[] bytes) {
        return Double.longBitsToDouble(toLong(bytes));
    }

    public static boolean isNumber(@Nullable String str) {
        return NumberUtils.isCreatable(str);
    }

    public static boolean isHexNumber(@Nullable String value) {
        if (StringUtils.isEmpty(value)) return false;
        int index = value.startsWith("-") ? 1 : 0;
        return value.startsWith("0x", index) || value.startsWith("0X", index) || value.startsWith("#", index);
    }

    public static int toInt(@NotNull String str) {
        return Integer.parseInt(str);
    }

    public static int toInt(@Nullable String str, int defaultValue) {
        return NumberUtils.toInt(str, defaultValue);
    }

    public static long toLong(@NotNull String str) {
        return Long.parseLong(str);
    }

    public static long toLong(@Nullable String str, long defaultValue) {
        return NumberUtils.toLong(str, defaultValue);
    }

    public static double toDouble(@NotNull String str) {
        if (str == null) throw new NumberFormatException("null");
        return Double.parseDouble(str);
    }

    public static double toDouble(@Nullable String str, double defaultValue) {
        return NumberUtils.toDouble(str, defaultValue);
    }

    public static Integer toIntObject(@NotNull String str) {
        return Integer.valueOf(str);
    }

    public static Integer toIntObject(@Nullable String str, Integer defaultValue) {
        if (StringUtils.isEmpty(str)) return defaultValue;
        try {
            return Integer.valueOf(str);
        } catch (final NumberFormatException nfe) {
            return defaultValue;
        }
    }

    public static Long toLongObject(@NotNull String str) {
        return Long.valueOf(str);
    }

    public static Long toLongObject(@Nullable String str, Long defaultValue) {
        if (StringUtils.isEmpty(str)) return defaultValue;
        try {
            return Long.valueOf(str);
        } catch (final NumberFormatException nfe) {
            return defaultValue;
        }
    }

    public static Double toDoubleObject(@NotNull String str) {
        if (str == null) throw new NumberFormatException("null");
        return Double.valueOf(str);
    }

    public static Double toDoubleObject(@Nullable String str, Double defaultValue) {
        if (StringUtils.isEmpty(str)) return defaultValue;
        try {
            return Double.valueOf(str);
        } catch (final NumberFormatException nfe) {
            return defaultValue;
        }
    }

    public static Integer hexToIntObject(@NotNull String str) {
        if (str == null) throw new NumberFormatException("null");
        return Integer.decode(str);
    }

    public static Integer hexToIntObject(@Nullable String str, Integer defaultValue) {
        if (StringUtils.isEmpty(str)) return defaultValue;
        try {
            return Integer.decode(str);
        } catch (final NumberFormatException nfe) {
            return defaultValue;
        }
    }

    public static Long hexToLongObject(@NotNull String str) {
        if (str == null) throw new NumberFormatException("null");
        return Long.decode(str);
    }

    public static Long hexToLongObject(@Nullable String str, Long defaultValue) {
        if (StringUtils.isEmpty(str)) return defaultValue;
        try {
            return Long.decode(str);
        } catch (final NumberFormatException nfe) {
            return defaultValue;
        }
    }

    public static String toString(int i) {
        return Integer.toString(i);
    }

    public static String toString(@NotNull Integer i) {
        return i.toString();
    }

    public static String toString(long l) {
        return Long.toString(l);
    }

    public static String toString(@NotNull Long l) {
        return l.toString();
    }

    public static String toString(double d) {
        return Double.toString(d);
    }

    public static String toString(@NotNull Double d) {
        return d.toString();
    }

    public static String to2DigitString(double d) {
        return String.format(Locale.ROOT, "%.2f", d);
    }

    public static int toInt32(long x) {
        if ((int) x == x) return (int) x;
        throw new IllegalArgumentException("Int " + x + " out of range");
    }
}