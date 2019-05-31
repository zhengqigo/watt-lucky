package org.fuelteam.watt.lucky.text;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.fuelteam.watt.lucky.annotation.Nullable;

/**
 * 从AndroidUtilCode的RegexUtils移植, 性能优化将正则表达式为预编译, 并修改了TEL的正则表达式.
 * 
 * @see https://github.com/Blankj/AndroidUtilCode/blob/master/utilcode/src/main/java/com/blankj/utilcode/util/RegexUtils.java
 * @see https://github.com/Blankj/AndroidUtilCode/blob/master/utilcode/src/main/java/com/blankj/utilcode/constant/RegexConstants.java
 */
public class TextValidator {

    private static final String REGEX_MOBILE_SIMPLE = "^[1]\\d{10}$";
    private static final Pattern PATTERN_REGEX_MOBILE_SIMPLE = Pattern.compile(REGEX_MOBILE_SIMPLE);

    /**
     * 手机号精确正则: 已知3位前缀＋8位数字
     * <p>
     * 移动：134(0-8)、135、136、137、138、139、147、150、151、152、157、158、159、178、182、183、184、187、188、198
     * </p>
     * <p>
     * 联通：130、131、132、145、155、156、166、171、175、176、185、186
     * </p>
     * <p>
     * 电信：133、153、173、177、180、181、189、199
     * </p>
     * <p>
     * 全球星：1349
     * </p>
     * <p>
     * 虚拟运营商：170
     * </p>
     */
    public static final String REGEX_MOBILE_EXACT = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(16[6])|(17[0,1,3,5-8])|(18[0-9])|(19[8,9]))\\d{8}$";
    private static final Pattern PATTERN_REGEX_MOBILE_EXACT = Pattern.compile(REGEX_MOBILE_EXACT);

    private static final String REGEX_TEL = "^(\\d{3,4}-)?\\d{6,8}$";
    private static final Pattern PATTERN_REGEX_TEL = Pattern.compile(REGEX_TEL);

    private static final String REGEX_ID_CARD15 = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$";
    private static final Pattern PATTERN_REGEX_ID_CARD15 = Pattern.compile(REGEX_ID_CARD15);

    private static final String REGEX_ID_CARD18 = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9Xx])$";
    private static final Pattern PATTERN_REGEX_ID_CARD18 = Pattern.compile(REGEX_ID_CARD18);

    private static final String REGEX_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
    private static final Pattern PATTERN_REGEX_EMAIL = Pattern.compile(REGEX_EMAIL);

    private static final String REGEX_URL = "[a-zA-z]+://[^\\s]*";
    private static final Pattern PATTERN_REGEX_URL = Pattern.compile(REGEX_URL);

    private static final String REGEX_DATE = "^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)$";
    private static final Pattern PATTERN_REGEX_DATE = Pattern.compile(REGEX_DATE);

    private static final String REGEX_IP = "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)";
    private static final Pattern PATTERN_REGEX_IP = Pattern.compile(REGEX_IP);

    public static boolean isMobileSimple(@Nullable CharSequence input) {
        return isMatch(PATTERN_REGEX_MOBILE_SIMPLE, input);
    }

    public static boolean isMobileExact(@Nullable CharSequence input) {
        return isMatch(PATTERN_REGEX_MOBILE_EXACT, input);
    }

    public static boolean isTel(@Nullable CharSequence input) {
        return isMatch(PATTERN_REGEX_TEL, input);
    }

    public static boolean isIdCard(@Nullable CharSequence input) {
        return isMatch(PATTERN_REGEX_ID_CARD15, input) || isMatch(PATTERN_REGEX_ID_CARD18, input);
    }

    public static boolean isEmail(@Nullable CharSequence input) {
        return isMatch(PATTERN_REGEX_EMAIL, input);
    }

    public static boolean isUrl(@Nullable CharSequence input) {
        return isMatch(PATTERN_REGEX_URL, input);
    }

    public static boolean isDate(@Nullable CharSequence input) {
        return isMatch(PATTERN_REGEX_DATE, input);
    }

    public static boolean isIp(@Nullable CharSequence input) {
        return isMatch(PATTERN_REGEX_IP, input);
    }

    public static boolean isMatch(Pattern pattern, CharSequence input) {
        return StringUtils.isNotEmpty(input) && pattern.matcher(input).matches();
    }
}