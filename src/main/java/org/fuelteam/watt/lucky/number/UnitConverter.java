package org.fuelteam.watt.lucky.number;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @see https://github.com/facebook/jcommon/blob/master/config/src/main/java/com/facebook/config/ConfigUtil.java
 */
public class UnitConverter {

    private static final long K = 1024L;
    private static final long M = K * 1024;
    private static final long G = M * 1024;
    private static final long T = G * 1024;

    private static final long MILLIS_PER_SECOND = 1000L;
    private static final long MILLIS_PER_MINUTE = MILLIS_PER_SECOND * 60;
    private static final long MILLIS_PER_HOUR = MILLIS_PER_MINUTE * 60;
    private static final long MILLIS_PER_DAY = MILLIS_PER_HOUR * 24;

    private static final Pattern NUMBER_AND_UNIT = Pattern.compile("(\\d+)([a-zA-Z]+)?");

    /**
     * 将带单位时间ms(毫秒),s(秒),m(分钟),h(小时),d(日),y(年)转化为毫秒数
     */
    public static long toDurationMillis(String duration) {
        Matcher matcher = NUMBER_AND_UNIT.matcher(duration);
        if (!matcher.matches()) throw new IllegalArgumentException("malformed duration string: " + duration);
        long number = Long.parseLong(matcher.group(1));
        String unitStr = matcher.group(2);
        if (unitStr == null) return number;
        char unit = unitStr.toLowerCase().charAt(0);
        switch (unit) {
            case 's':
                return number * MILLIS_PER_SECOND;
            case 'm':
                if (unitStr.length() >= 2 && unitStr.charAt(1) == 's') return number;
                return number * MILLIS_PER_MINUTE;
            case 'h':
                return number * MILLIS_PER_HOUR;
            case 'd':
                return number * MILLIS_PER_DAY;
            default:
                throw new IllegalArgumentException("unknown time unit :" + unit);
        }
    }

    /**
     * 将带单位的大小b(b),k(kb),m(mb),g(gb),t(tb)转化为字节数
     */
    public static long toBytes(String size) {
        Matcher matcher = NUMBER_AND_UNIT.matcher(size);
        if (!matcher.matches()) throw new IllegalArgumentException("malformed size string: " + size);
        long number = Long.parseLong(matcher.group(1));
        String unitStr = matcher.group(2);
        if (unitStr == null) return number;
        char unit = unitStr.toLowerCase().charAt(0);
        switch (unit) {
            case 'b':
                return number;
            case 'k':
                return number * K;
            case 'm':
                return number * M;
            case 'g':
                return number * G;
            case 't':
                return number * T;
            default:
                throw new IllegalArgumentException("unknown size unit :" + unit);
        }
    }

    /**
     * 从bytes转换为带单位的字符串, 单位最大只支持到G级别, 四舍五入
     * 
     * @param scale 小数后的精度
     */
    public static String toSizeUnit(Long bytes, int scale) {
        if (bytes == null) return "n/a";
        if (bytes < K) return String.format("%4d", bytes);
        if (bytes < M) return String.format("%" + (scale == 0 ? 4 : 5 + scale) + '.' + scale + "fk", bytes * 1d / K);
        if (bytes < G) return String.format("%" + (scale == 0 ? 4 : 5 + scale) + '.' + scale + "fm", bytes * 1d / M);
        if (bytes < T) return String.format("%" + (scale == 0 ? 4 : 5 + scale) + '.' + scale + "fg", bytes * 1d / G);
        return String.format("%" + (scale == 0 ? 4 : 5 + scale) + '.' + scale + "ft", bytes * 1d / T);
    }

    /**
     * 转换毫秒为带时间单位的字符串, 单位最大到day级别, 四舍五入
     * 
     * @param scale 小数后的精度
     */
    public static String toTimeUnit(long millis, int scale) {
        if (millis < MILLIS_PER_SECOND) return String.format("%4dms", millis);
        if (millis < MILLIS_PER_MINUTE)
            return String.format("%" + (scale == 0 ? 2 : 3 + scale) + '.' + scale + "fs", millis * 1d / MILLIS_PER_SECOND);
        if (millis < MILLIS_PER_HOUR)
            return String.format("%" + (scale == 0 ? 2 : 3 + scale) + '.' + scale + "fm", millis * 1d / MILLIS_PER_MINUTE);
        if (millis < MILLIS_PER_DAY)
            return String.format("%" + (scale == 0 ? 2 : 3 + scale) + '.' + scale + "fh", millis * 1d / MILLIS_PER_HOUR);
        return String.format("%" + (scale == 0 ? 2 : 3 + scale) + '.' + scale + "fd", millis * 1d / MILLIS_PER_DAY);
    }

    /**
     * 转换毫秒为带时间单位的字符串, 会同时带下一级的单位, 四舍五入
     */
    public static String toTimeWithMinorUnit(long millis) {
        if (millis < MILLIS_PER_SECOND) return String.format("%4dms", millis);
        if (millis < MILLIS_PER_MINUTE) return String.format("%02ds", millis / MILLIS_PER_SECOND);
        if (millis < MILLIS_PER_HOUR)
            return String.format("%02dm%02ds", millis / MILLIS_PER_MINUTE, (millis / MILLIS_PER_SECOND) % 60);
        if (millis < MILLIS_PER_DAY)
            return String.format("%02dh%02dm", millis / MILLIS_PER_HOUR, (millis / MILLIS_PER_MINUTE) % 60);
        return String.format("%dd%02dh", millis / MILLIS_PER_DAY, (millis / MILLIS_PER_HOUR) % 24);
    }
}