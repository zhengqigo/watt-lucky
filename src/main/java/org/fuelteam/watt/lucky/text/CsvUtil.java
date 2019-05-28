package org.fuelteam.watt.lucky.text;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

/**
 * 从Jodd移植Csv工具类
 * <pre>
 * https://github.com/oblac/jodd/blob/master/jodd-core/src/main/java/jodd/util/CsvUtil.java
 */
public class CsvUtil {

    protected static final char FIELD_SEPARATOR = ',';
    protected static final char FIELD_QUOTE = '"';
    protected static final String DOUBLE_QUOTE = "\"\"";
    protected static final String SPECIAL_CHARS = "\r\n";
    protected static final String SPACE = " ";
    protected static final String QUOTE = "\"";

    public static String toCsvString(Object... elements) {
        StringBuilder line = new StringBuilder();
        int last = elements.length - 1;
        for (int i = 0; i < elements.length; i++) {
            if (elements[i] == null) {
                if (i != last) line.append(FIELD_SEPARATOR);
                continue;
            }
            String field = elements[i].toString();

            // check for special cases
            int ndx = field.indexOf(FIELD_SEPARATOR);
            if (ndx == -1) ndx = field.indexOf(FIELD_QUOTE);
            if (ndx == -1 && (field.startsWith(SPACE) || field.endsWith(SPACE))) ndx = 1;
            if (ndx == -1) ndx = StringUtils.indexOf(field, SPECIAL_CHARS);
            if (ndx != -1) line.append(FIELD_QUOTE);
            field = StringUtils.replace(field, QUOTE, DOUBLE_QUOTE);
            line.append(field);
            if (ndx != -1) line.append(FIELD_QUOTE);
            if (i != last) line.append(FIELD_SEPARATOR);
        }
        return line.toString();
    }

    public static String[] fromCsvString(String line) {
        List<String> row = Lists.newArrayList();

        boolean inQuotedField = false;
        int fieldStart = 0;

        final int len = line.length();
        for (int i = 0; i < len; i++) {
            char c = line.charAt(i);
            if (c == FIELD_SEPARATOR) {
                if (!inQuotedField) { // ignore we are quoting
                    addField(row, line, fieldStart, i, inQuotedField);
                    fieldStart = i + 1;
                }
            } else if (c == FIELD_QUOTE) {
                if (inQuotedField) {
                    if (i + 1 == len || line.charAt(i + 1) == FIELD_SEPARATOR) { // we are already quoting - peek to see
                        // if this is the end of the field
                        addField(row, line, fieldStart, i, inQuotedField);
                        fieldStart = i + 2;
                        i++; // and skip the comma
                        inQuotedField = false;
                    }
                } else if (fieldStart == i) {
                    inQuotedField = true; // this is a beginning of a quote
                    fieldStart++; // move field start
                }
            }
        }
        // add last field - but only if string was not empty
        if (len > 0 && fieldStart <= len) addField(row, line, fieldStart, len, inQuotedField);
        return row.toArray(new String[row.size()]);
    }

    private static void addField(List<String> row, String line, int startIndex, int endIndex, boolean inQuoted) {
        String field = line.substring(startIndex, endIndex);
        if (inQuoted) field = StringUtils.replace(field, DOUBLE_QUOTE, "\"");
        row.add(field);
    }
}