package org.fuelteam.watt.lucky.text;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.fuelteam.watt.lucky.annotation.Nullable;
import org.fuelteam.watt.lucky.utils.ListUtil;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.base.Utf8;
import com.google.common.collect.Lists;

public class MoreStringUtil {

    public static List<String> split(@Nullable final String str, final char separatorChar) {
        return split(str, separatorChar, 10);
    }

    public static List<String> split(@Nullable final String str, final char separatorChar, int expectParts) {
        if (str == null) return null;

        final int len = str.length();
        if (len == 0) return ListUtil.emptyList();

        final List<String> list = Lists.newArrayListWithCapacity(expectParts);
        int i = 0;
        int start = 0;
        boolean match = false;
        while (i < len) {
            if (str.charAt(i) == separatorChar) {
                if (match) {
                    list.add(str.substring(start, i));
                    match = false;
                }
                start = ++i;
                continue;
            }
            match = true;
            i++;
        }
        if (match) list.add(str.substring(start, i));
        return list;
    }

    /**
     * @see com.google.common.base.Splitter
     */
    public static Splitter charsSplitter(final String separatorChars) {
        return Splitter.on(CharMatcher.anyOf(separatorChars));
    }

    public static String replaceFirst(@Nullable String s, char sub, char with) {
        if (s == null) return null;
        int index = s.indexOf(sub);
        if (index == -1) return s;
        char[] str = s.toCharArray();
        str[index] = with;
        return new String(str);
    }

    public static String replaceLast(@Nullable String s, char sub, char with) {
        if (s == null) return null;

        int index = s.lastIndexOf(sub);
        if (index == -1) return s;
        char[] str = s.toCharArray();
        str[index] = with;
        return new String(str);
    }

    public static boolean startWith(@Nullable CharSequence s, char c) {
        if (StringUtils.isEmpty(s)) return false;
        return s.charAt(0) == c;
    }

    public static boolean endWith(@Nullable CharSequence s, char c) {
        if (StringUtils.isEmpty(s)) return false;
        return s.charAt(s.length() - 1) == c;
    }

    public static String removeEnd(final String s, final char c) {
        if (endWith(s, c)) return s.substring(0, s.length() - 1);
        return s;
    }

    /**
     * @see Utf8#encodedLength(CharSequence)
     */
    public static int utf8EncodedLength(@Nullable CharSequence sequence) {
        if (StringUtils.isEmpty(sequence)) return 0;
        return Utf8.encodedLength(sequence);
    }
}