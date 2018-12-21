package org.fuelteam.watt.lucky.utils;

import java.util.Date;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateUtil {

    public static Date str2date(String dateStr, String pattern) throws Exception {
        DateTimeFormatter dtFormatter = DateTimeFormat.forPattern(pattern);
        return dtFormatter.parseDateTime(dateStr).toDate();
    }

    public static Date str2date(String dateStr, String pattern, Locale locale) throws Exception {
        DateTimeFormatter dtFormatter = DateTimeFormat.forPattern(pattern).withLocale(locale);
        return dtFormatter.parseDateTime(dateStr).toDate();
    }

    public static Date str2date(String dateStr, String pattern, int hours) throws Exception {
        Date date = str2date(dateStr, pattern);
        if (date == null) return null;
        return new DateTime(date).plusHours(hours).toDate();
    }

    public static Date now() {
        return new DateTime().toDate();
    }

    public static String date2str(Date date, String pattern) {
        return new DateTime(date).toString(pattern);
    }

    public static String str2str(String dateStr, String patternFrom, String patternTo) throws Exception {
        Date date = str2date(dateStr, patternFrom);
        if (date == null) {
            return null;
        }
        return date2str(date, patternTo);
    }

    public static String str2str(String dateStr, String patternFrom, String patternTo, String symbol, int plus)
            throws Exception {
        Date date = str2date(dateStr, patternFrom);
        DateTime datetime = new DateTime(date);
        if ("h".equalsIgnoreCase(symbol)) {
            datetime.plusHours(plus);
        }
        if ("m".equalsIgnoreCase(symbol)) {
            datetime.plusMinutes(plus);
        }
        return date2str(datetime.toDate(), patternTo);
    }

    public static String clean(String dateStr) {
        return dateStr.replaceAll("[-:\\s]", "");
    }
}
