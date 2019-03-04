package org.fuelteam.watt.lucky.utils;

import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public class StringUtil extends StringUtils {

    public static String clean(final String str) {
        return isEmpty(str) ? "" : str.trim();
    }

    public static String clean(final Object object) {
        return (object == null) ? null : String.valueOf(object);
    }

    public static String cleanAs(final String str, final String define) {
        return isEmpty(str) ? clean(define) : str.trim();
    }

    public static Integer cleanAs(final String str, final Integer define) {
        return integerAndPositive(str) ? define : Integer.parseInt(str);
    }

    public static boolean integer(final String str) {
        return match(str, "-?\\d+");
    }

    public static boolean integerAndPositive(final String str) {
        return integer(str) && positive(str);
    }

    public static String cleanEx(String clean) {
        if (clean == null) return null;
        return clean(clean);
    }

    public static Double str2dbl(String str, Double defDbl) {
        String cleanedStr = cleanEx(str);
        return StringUtils.isEmpty(cleanedStr) && numeric(cleanedStr) ? defDbl : Double.parseDouble(cleanedStr);
    }

    public static BigDecimal str2bd(String str, BigDecimal defBd) {
        String cleanedStr = cleanEx(str);
        return StringUtils.isEmpty(cleanedStr) && numeric(cleanedStr) ? defBd : new BigDecimal(cleanedStr);
    }

    public static String clean(JSONArray jsonArray, String defVal, String... fields) {
        Preconditions.checkNotNull(jsonArray, "jsonArray can not be null");
        Preconditions.checkArgument(fields == null || fields.length <= 0, "fields can not be null or empty");
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
        Preconditions.checkNotNull(jsonObject, "jsonObject can not be null");
        Preconditions.checkNotNull(key, "key can not be null");
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
        return str.charAt(0) == '-';
    }

    public static boolean positive(final String str) {
        return str.charAt(0) != '-';
    }

    // 是否匹配规则
    public static boolean match(final String str, final String pattern) {
        return str.matches(pattern);
    }

    public static String[] matchAndGet(final String str, final String pattern) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(str);
        List<String> group = Lists.newArrayList();
        Integer index = 1;
        while (m.find()) {
            group.add(m.group(index++));
        }
        return group.toArray(new String[] {});
    }

    // 去掉小数点后无效0
    public static String trim0(String source) {
        if (source.indexOf(".") <= 0) return source;
        source = source.replaceAll("0+?$", "");
        source = source.replaceAll("[.]$", "");
        return source;
    }
}
