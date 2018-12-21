package org.fuelteam.watt.lucky.utils;

import org.apache.commons.lang3.StringUtils;

public class StringUtil extends StringUtils {

    // 当参数为null或长度为零时返回""，否则返回trim后的字串
    public static String clean(final String clean) {
        return isEmpty(clean) ? "" : clean.trim();
    }

    // 当参数为null或长度为零时返回defVal，否则返回trim后的字串
    public static String cleanAs(final String clean, final String defVal) {
        return isEmpty(clean) ? clean(defVal) : clean.trim();
    }

    // 是否为数字
    public static boolean numeric(final String str) {
        return match(str, "[-+]?[0-9]?[.]{0,1}[0-9]*");
    }

    // 是否为负数
    public static boolean negative(final String str) {
        return match(str, "[-+]?[0-9]?[.]{0,1}[0-9]*") && str.charAt(0) == '-';
    }

    // 是否匹配规则
    public static boolean match(final String str, final String pattern) {
        return str.matches(pattern);
    }

    // 去掉小数点后无效0
    public static String trim0(String source) {
        if (source.indexOf(".") <= 0) return source;
        source = source.replaceAll("0+?$", "");
        source = source.replaceAll("[.]$", "");
        return source;
    }
}
