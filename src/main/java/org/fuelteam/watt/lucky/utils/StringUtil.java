package org.fuelteam.watt.lucky.utils;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class StringUtil extends StringUtils {

    // 当参数为null或长度为零时返回""，否则返回trim后的字串
    public static String clean(final String clean) {
        return isEmpty(clean) ? "" : clean.trim();
    }

    // 当参数为null或长度为零时返回defVal，否则返回trim后的字串
    public static String cleanAs(final String clean, final String defVal) {
        return isEmpty(clean) ? clean(defVal) : clean.trim();
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
        if (jsonArray == null || fields == null || fields.length <= 0) return null;
        String result = "";
        for (String field : fields) {
            if (field == null) continue;
            String value = byName(jsonArray, "Name", field, "DataType");
            if (!StringUtils.isEmpty(value)) {
                result = value;
                break;
            }
        }
        return cleanAs(result, defVal);
    }
    
    public static String byName(JSONArray array, String field, String name, String key) {
        String result = "";
        for (int i = 0; i < array.size(); i++) {
            JSONObject object = array.getJSONObject(i);
            if (name.equals(object.getString(field))) {
                result = object.getString(object.getString(key));
                break;
            }
        }
        return clean(result);
    }

    // 是否为数字
    public static boolean numeric(final String str) {
        return match(str, "[-+]?[0-9]+[.]{0,1}[0-9]*");
    }

    // 是否为负数
    public static boolean negative(final String str) {
        return match(str, "[-+]?[0-9]+[.]{0,1}[0-9]*") && str.charAt(0) == '-';
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
