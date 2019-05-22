package org.fuelteam.watt.lucky.time;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateUtils;
import org.fuelteam.watt.lucky.annotation.NotNull;

public class DateUtil {

    public static final long MILLIS_PER_SECOND = 1000;
    public static final long MILLIS_PER_MINUTE = 60 * MILLIS_PER_SECOND;
    public static final long MILLIS_PER_HOUR = 60 * MILLIS_PER_MINUTE;
    public static final long MILLIS_PER_DAY = 24 * MILLIS_PER_HOUR;

    private static final int[] MONTH_LENGTH = { 0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

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
}