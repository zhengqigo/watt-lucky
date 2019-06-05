package org.fuelteam.watt.lucky.time;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.fuelteam.watt.lucky.annotation.NotNull;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.google.common.collect.Maps;

public class DateUtil {

    public static final long MILLIS_PER_SECOND = 1000;
    public static final long MILLIS_PER_MINUTE = 60 * MILLIS_PER_SECOND;
    public static final long MILLIS_PER_HOUR = 60 * MILLIS_PER_MINUTE;
    public static final long MILLIS_PER_DAY = 24 * MILLIS_PER_HOUR;

    private static final int[] MONTH_LENGTH = { 0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
    
    // 以T分隔日期和时间，并带时区信息，符合ISO8601规范
    public static final String PATTERN_ISO = "yyyy-MM-dd'T'HH:mm:ss.SSSZZ";
    public static final String PATTERN_ISO_ON_SECOND = "yyyy-MM-dd'T'HH:mm:ssZZ";
    public static final String PATTERN_ISO_ON_DATE = "yyyy-MM-dd";

    // 以空格分隔日期和时间，不带时区信息
    public static final String PATTERN_DEFAULT = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String PATTERN_DEFAULT_ON_SECOND = "yyyy-MM-dd HH:mm:ss";

    // 使用工厂方法FastDateFormat.getInstance(), 从缓存中获取实例

    // 以T分隔日期和时间，并带时区信息，符合ISO8601规范
    public static final FastDateFormat ISO_FORMAT = FastDateFormat.getInstance(PATTERN_ISO);
    public static final FastDateFormat ISO_ON_SECOND_FORMAT = FastDateFormat.getInstance(PATTERN_ISO_ON_SECOND);
    public static final FastDateFormat ISO_ON_DATE_FORMAT = FastDateFormat.getInstance(PATTERN_ISO_ON_DATE);

    // 以空格分隔日期和时间，不带时区信息
    public static final FastDateFormat DEFAULT_FORMAT = FastDateFormat.getInstance(PATTERN_DEFAULT);
    public static final FastDateFormat DEFAULT_ON_SECOND_FORMAT = FastDateFormat.getInstance(PATTERN_DEFAULT_ON_SECOND);

    private static volatile Map<String, DateTimeFormatter> map = Maps.newConcurrentMap();

    {
        map.put("yyyyMMdd", DateTimeFormat.forPattern("yyyyMMdd"));
        map.put("yyyy-MM-dd", DateTimeFormat.forPattern("yyyy-MM-dd"));
        map.put("yyyyMMddHHmmss", DateTimeFormat.forPattern("yyyyMMddHHmmss"));
        map.put("yyyyMMddHHmm00", DateTimeFormat.forPattern("yyyyMMddHHmm00"));
        map.put("yyyy-MM-dd HH:mm:ss", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        map.put("yyyyMMddHHmmssSSS", DateTimeFormat.forPattern("yyyyMMddHHmmssSSS"));
        map.put("yyyy-MM-dd HH:mm:ss.SSS", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS"));
    }
    
    public static Date addDays(@NotNull final Date date, final int amount) {
        return DateUtils.addDays(date, amount);
    }

    public static Date subDays(@NotNull final Date date, int amount) {
        return DateUtils.addDays(date, -amount);
    }

    /**
     * 获得日期是所在周的第几天，Monday=1
     */
    public static int getDayOfWeek(@NotNull final Date date) {
        int result = getWithMondayFirst(date, Calendar.DAY_OF_WEEK);
        return result == 1 ? 7 : result - 1;
    }

    /**
     * 获得日期是所在年的第几天，从1开始
     */
    public static int getDayOfYear(@NotNull final Date date) {
        return get(date, Calendar.DAY_OF_YEAR);
    }

    /**
     * 获得日期是所在月的第几周，从1开始，只要有一天在该月都算进来，Monday=1
     */
    public static int getWeekOfMonth(@NotNull final Date date) {
        return getWithMondayFirst(date, Calendar.WEEK_OF_MONTH);
    }

    /**
     * 获得日期是一年的第几周，返回值从1开始，只要有一天在该月都算进来，Monday=1
     */
    public static int getWeekOfYear(@NotNull final Date date) {
        return getWithMondayFirst(date, Calendar.WEEK_OF_YEAR);
    }

    private static int get(final Date date, int field) {
        Validate.notNull(date, "The date must not be null");
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(field);
    }

    private static int getWithMondayFirst(final Date date, int field) {
        Validate.notNull(date, "The date must not be null");
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.setTime(date);
        return cal.get(field);
    }

    public static Date beginOfYear(@NotNull final Date date) {
        return DateUtils.truncate(date, Calendar.YEAR);
    }

    public static Date endOfYear(@NotNull final Date date) {
        return new Date(nextYear(date).getTime() - 1);
    }

    public static Date nextYear(@NotNull final Date date) {
        return DateUtils.ceiling(date, Calendar.YEAR);
    }

    public static Date beginOfMonth(@NotNull final Date date) {
        return DateUtils.truncate(date, Calendar.MONTH);
    }

    public static Date endOfMonth(@NotNull final Date date) {
        return new Date(nextMonth(date).getTime() - 1);
    }

    public static Date nextMonth(@NotNull final Date date) {
        return DateUtils.ceiling(date, Calendar.MONTH);
    }

    public static Date beginOfWeek(@NotNull final Date date) {
        return DateUtils.truncate(DateUtil.subDays(date, DateUtil.getDayOfWeek(date) - 1), Calendar.DATE);
    }

    public static Date endOfWeek(@NotNull final Date date) {
        return new Date(nextWeek(date).getTime() - 1);
    }

    public static Date nextWeek(@NotNull final Date date) {
        return DateUtils.truncate(DateUtil.addDays(date, 8 - DateUtil.getDayOfWeek(date)), Calendar.DATE);
    }

    public static Date beginOfDate(@NotNull final Date date) {
        return DateUtils.truncate(date, Calendar.DATE);
    }

    public static Date endOfDate(@NotNull final Date date) {
        return new Date(nextDate(date).getTime() - 1);
    }

    public static Date nextDate(@NotNull final Date date) {
        return DateUtils.ceiling(date, Calendar.DATE);
    }

    public static Date beginOfHour(@NotNull final Date date) {
        return DateUtils.truncate(date, Calendar.HOUR_OF_DAY);
    }

    public static Date endOfHour(@NotNull final Date date) {
        return new Date(nextHour(date).getTime() - 1);
    }

    public static Date nextHour(@NotNull final Date date) {
        return DateUtils.ceiling(date, Calendar.HOUR_OF_DAY);
    }

    public static Date beginOfMinute(@NotNull final Date date) {
        return DateUtils.truncate(date, Calendar.MINUTE);
    }

    public static Date endOfMinute(@NotNull final Date date) {
        return new Date(nextMinute(date).getTime() - 1);
    }

    public static Date nextMinute(@NotNull final Date date) {
        return DateUtils.ceiling(date, Calendar.MINUTE);
    }

    public static boolean isLeapYear(@NotNull final Date date) {
        return isLeapYear(get(date, Calendar.YEAR));
    }

    public static boolean isLeapYear(int y) {
        boolean result = false;
        if (((y % 4) == 0) && ((y < 1582) || ((y % 100) != 0) || ((y % 400) == 0))) result = true;
        return result;
    }

    public static int getMonthLength(@NotNull final Date date) {
        int year = get(date, Calendar.YEAR);
        int month = get(date, Calendar.MONTH);
        return getMonthLength(year, month);
    }

    public static int getMonthLength(int year, int month) {
        if ((month < 1) || (month > 12)) throw new IllegalArgumentException("Invalid month: " + month);
        if (month == 2) return isLeapYear(year) ? 29 : 28;
        return MONTH_LENGTH[month];
    }
    
    public static Date parseDate(@NotNull String pattern, @NotNull String dateString) throws ParseException {
        return FastDateFormat.getInstance(pattern).parse(dateString);
    }

    public static String formatDate(@NotNull String pattern, @NotNull Date date) {
        return FastDateFormat.getInstance(pattern).format(date);
    }

    public static String formatDate(@NotNull String pattern, long date) {
        return FastDateFormat.getInstance(pattern).format(date);
    }

    /**
     * 格式化时间间隔HH:mm:ss.SSS
     */
    public static String formatDuration(@NotNull Date startDate, @NotNull Date endDate) {
        return DurationFormatUtils.formatDurationHMS(endDate.getTime() - startDate.getTime());
    }

    /**
     * 格式化时间间隔HH:mm:ss.SSS
     */
    public static String formatDuration(long durationMillis) {
        return DurationFormatUtils.formatDurationHMS(durationMillis);
    }

    /**
     * 格式化时间间隔HH:mm:ss
     */
    public static String formatDurationOnSecond(@NotNull Date startDate, @NotNull Date endDate) {
        return DurationFormatUtils.formatDuration(endDate.getTime() - startDate.getTime(), "HH:mm:ss");
    }

    /**
     * 格式化时间间隔HH:mm:ss
     */
    public static String formatDurationOnSecond(long durationMillis) {
        return DurationFormatUtils.formatDuration(durationMillis, "HH:mm:ss");
    }

    /**
     * 打印用户友好的，与当前时间相比的时间差，如刚刚，5分钟前，今天XXX，昨天XXX
     */
    public static String formatFriendlyTimeSpanByNow(@NotNull Date date) {
        return formatFriendlyTimeSpanByNow(date.getTime());
    }

    /**
     * 打印用户友好、与当前时间相比的时间差，如刚刚，5分钟前，今天XXX，昨天XXX
     */
    public static String formatFriendlyTimeSpanByNow(long timeStampMillis) {
        long now = ClockUtil.currentTimeMillis();
        long span = now - timeStampMillis;
        if (span < 0) {
            // 'c' 日期和时间，被格式化为 "%ta %tb %td %tT %tZ %tY"，例如 "Sun Jul 20 16:17:00 EDT 1969"。
            return String.format("%tc", timeStampMillis);
        }
        if (span < DateUtil.MILLIS_PER_SECOND) {
            return "刚刚";
        } else if (span < DateUtil.MILLIS_PER_MINUTE) {
            return String.format("%d秒前", span / DateUtil.MILLIS_PER_SECOND);
        } else if (span < DateUtil.MILLIS_PER_HOUR) {
            return String.format("%d分钟前", span / DateUtil.MILLIS_PER_MINUTE);
        }
        // 获取当天00:00
        long wee = DateUtil.beginOfDate(new Date(now)).getTime();
        if (timeStampMillis >= wee) {
            // 'R' 24 小时制的时间，被格式化为 "%tH:%tM"
            return String.format("今天%tR", timeStampMillis);
        } else if (timeStampMillis >= wee - DateUtil.MILLIS_PER_DAY) {
            return String.format("昨天%tR", timeStampMillis);
        } else {
            // 'F' ISO 8601 格式的完整日期，被格式化为 "%tY-%tm-%td"。
            return String.format("%tF", timeStampMillis);
        }
    }
    


    public static Date str2date(String dateStr, String pattern) {
        return get(pattern).parseDateTime(dateStr).toDate();
    }

    public static boolean check(String dateStr, String pattern) {
        Date date = str2date(dateStr, pattern);
        if (date == null) return false;
        return true;
    }
    
    private final static DateTimeFormatter get(String pattern) {
        DateTimeFormatter dtf = map.get(pattern);
        if (dtf == null) {
            dtf = DateTimeFormat.forPattern(pattern);
            map.putIfAbsent(pattern, dtf);
        }
        return dtf;
    }

    public final static String long2str(long datetime, String pattern) {
        Date date = new Date(datetime);
        return new DateTime(date).toString(get(pattern));
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
        if ("mm".equalsIgnoreCase(symbol)) dt = dt.plusMonths(plus);
        if ("y".equalsIgnoreCase(symbol)) dt = dt.plusYears(plus);
        if ("s".equalsIgnoreCase(symbol)) dt = dt.plusSeconds(plus);
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

    public static Date minute(Date date) {
        return str2date(date2str(date, "yyyyMMddHHmm00"), "yyyyMMddHHmm00");
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
}