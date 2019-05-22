package org.fuelteam.watt.lucky.time;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.fuelteam.watt.lucky.annotation.NotNull;

public class DateFormatUtil {

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
}