package org.fuelteam.watt.lucky.utils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class StringUtil extends StringUtils {

    private final static Map<String, Pattern> map = Maps.newConcurrentMap();

    private final static Pattern get(String pattern) {
        Pattern p = map.get(pattern);
        if (p == null) {
            p = Pattern.compile(pattern);
            map.putIfAbsent(pattern, p);
        }
        return p;
    }

    public static String clean(final String str) {
        return isEmpty(str) ? "" : str.trim();
    }

    public static String clean(final Object object) {
        return (object == null) ? null : String.valueOf(object);
    }

    public static String cleanAs(final String str, final String def) {
        return isEmpty(str) ? clean(def) : str.trim();
    }

    public static Integer cleanAs(final String str, final Integer def) {
        return integer(str) ? Integer.parseInt(str) : def;
    }

    public static boolean integer(final String str) {
        return match(str, "-?\\d+");
    }

    public static String cleanEx(String clean) {
        if (clean == null) return null;
        return clean(clean);
    }

    public static Double str2dbl(String str, Double def) {
        String cleaned = clean(str);
        return isEmpty(cleaned) || !numeric(cleaned) ? def : Double.parseDouble(cleaned);
    }

    public static BigDecimal str2bd(String str, BigDecimal def) {
        String cleaned = clean(str);
        return isEmpty(cleaned) || !numeric(cleaned) ? def : new BigDecimal(cleaned);
    }

    public static String clean(JSONArray jsonArray, String defVal, String... fields) {
        String result = "";
        for (String field : fields) {
            if (field == null) continue;
            String value = byName(jsonArray, "Name", field, "DataType");
            if (StringUtils.isEmpty(value)) continue;
            result = value;
            break;
        }
        return cleanAs(result, defVal);
    }

    public static String clean(JSONObject jsonObject, String key) {
        Object object = jsonObject.get(key);
        if (object == null) return null;
        return String.valueOf(object).trim();
    }

    public static String byName(JSONArray array, String field, String name, String key) {
        String result = "";
        for (int i = 0; i < array.size(); i++) {
            JSONObject object = array.getJSONObject(i);
            if (!name.equalsIgnoreCase(object.getString(field))) continue;
            result = object.getString(object.getString(key));
            break;
        }
        return clean(result);
    }

    // 是否为数字
    public static boolean numeric(final String str) {
        return match(str, "[-+]?[0-9]+[.]{0,1}[0-9]*");
    }

    // 是否为负数
    public static boolean negative(final String str) {
        return numeric(str) && str.charAt(0) == '-';
    }

    public static boolean positive(final String str) {
        return numeric(str) && str.charAt(0) != '-';
    }

    // 是否匹配规则
    public static boolean match(final String str, final String pattern) {
        Matcher matcher = get(pattern).matcher(str);
        return matcher.matches();
    }

    public static String[] matchAndGet(final String str, final String pattern) {
        Matcher m = get(pattern).matcher(str);
        List<String> group = Lists.newArrayList();
        Integer index = 0;
        while (m.find()) {
            group.add(m.group(index++));
        }
        return group.toArray(new String[] {});
    }

    // 去掉小数点后无效0: 123.00 -> 123
    public static String trim0(String source) {
        if (numeric(source) && source.indexOf(".") > 0) return source.replaceAll(".0+?$", "");
        return source;
    }
}
