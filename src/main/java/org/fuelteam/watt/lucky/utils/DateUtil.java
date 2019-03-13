package org.fuelteam.watt.lucky.utils;

import java.util.Date;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateUtil {

    public static Date str2date(String dateStr, String pattern) {
        DateTimeFormatter dtf = DateTimeFormat.forPattern(pattern);
        return dtf.parseDateTime(dateStr).toDate();
    }

    public static boolean check(String dateStr, String pattern) {
        Date date = str2date(dateStr, pattern);
        if (date == null) return false;
        return true;
    }

    public final static String long2str(final long datetime) {
        return new DateTime(new Date(datetime)).toString("yyyy-MM-dd HH:mm:ss.SSS");
    }
    
    public final static Date prev(Date date) {
        DateTime dt = new DateTime(date);
        int index = dt.getDayOfWeek();
        if (index == 6) return dt.minusDays(1).toDate();
        if (index == 7) return dt.minusDays(2).toDate();
        if (index == 1) return dt.minusDays(3).toDate();
        return dt.minusDays(1).toDate();
    }
    
    public final static Date next(Date date) {
        DateTime dt = new DateTime(date);
        int index = dt.getDayOfWeek();
        if (index == 5) return dt.plusDays(3).toDate();
        if (index == 6) return dt.plusDays(2).toDate();
        if (index == 7) return dt.plusDays(1).toDate();
        return dt.plusDays(1).toDate();
    }

    public static Date str2date(String dateStr, String pattern, Locale locale) {
        DateTimeFormatter dtFormatter = DateTimeFormat.forPattern(pattern).withLocale(locale);
        return dtFormatter.parseDateTime(dateStr).toDate();
    }

    public static Date date2date(Date date, String symbol, int plus) {
        DateTime datetime = symbol(new DateTime(date), symbol, plus);
        return datetime.toDate();
    }

    private static DateTime symbol(DateTime datetime, String symbol, int plus) {
        DateTime dt = datetime;
        if ("d".equalsIgnoreCase(symbol)) dt = dt.plusDays(plus);
        if ("h".equalsIgnoreCase(symbol)) dt = dt.plusHours(plus);
        if ("m".equalsIgnoreCase(symbol)) dt = dt.plusMinutes(plus);
        return dt;
    }

    public static Date str2date(String dateStr, String pattern, String symbol, int plus) {
        Date date = str2date(dateStr, pattern);
        if (date == null) return null;
        DateTime datetime = symbol(new DateTime(date), symbol, plus);
        return datetime.toDate();
    }

    public static Date now() {
        return new DateTime().toDate();
    }

    public static String date2str(Date date, String pattern) {
        return date2str(date, pattern, "h", 0);
    }

    public static String date2str(Date date, String pattern, String symbol, int plus) {
        DateTime datetime = symbol(new DateTime(date), symbol, plus);
        return datetime.toString(pattern);
    }

    public static String str2str(String dateStr, String patternFrom, String patternTo) {
        Date date = str2date(dateStr, patternFrom);
        if (date == null) return null;
        return date2str(date, patternTo);
    }

    public static String str2str(String dateStr, String patternFrom, String patternTo, String symbol, int plus) {
        Date date = str2date(dateStr, patternFrom);
        DateTime datetime = symbol(new DateTime(date), symbol, plus);
        return date2str(datetime.toDate(), patternTo);
    }

    public static String clean(String dateStr) {
        return dateStr.replaceAll("[-:\\s]", "");
    }
}
