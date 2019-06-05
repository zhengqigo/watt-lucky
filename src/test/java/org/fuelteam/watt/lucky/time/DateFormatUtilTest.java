package org.fuelteam.watt.lucky.time;

import static org.assertj.core.api.Assertions.assertThat;

import java.text.ParseException;
import java.util.Date;

import org.junit.Test;

@SuppressWarnings("deprecation")
public class DateFormatUtilTest {

    @Test
    public void isoDateFormat() {
        Date date = new Date(116, 10, 1, 12, 23, 44);
        assertThat(DateUtil.ISO_FORMAT.format(date)).contains("2016-11-01T12:23:44.000");
        assertThat(DateUtil.ISO_ON_SECOND_FORMAT.format(date)).contains("2016-11-01T12:23:44");
        assertThat(DateUtil.ISO_ON_DATE_FORMAT.format(date)).isEqualTo("2016-11-01");
    }

    @Test
    public void defaultDateFormat() {
        Date date = new Date(116, 10, 1, 12, 23, 44);
        assertThat(DateUtil.DEFAULT_FORMAT.format(date)).isEqualTo("2016-11-01 12:23:44.000");
        assertThat(DateUtil.DEFAULT_ON_SECOND_FORMAT.format(date)).isEqualTo("2016-11-01 12:23:44");
    }

    @Test
    public void formatWithPattern() {
        Date date = new Date(116, 10, 1, 12, 23, 44);
        assertThat(DateUtil.formatDate(DateUtil.PATTERN_DEFAULT, date)).isEqualTo("2016-11-01 12:23:44.000");
        assertThat(DateUtil.formatDate(DateUtil.PATTERN_DEFAULT, date.getTime()))
                .isEqualTo("2016-11-01 12:23:44.000");
    }

    @Test
    public void parseWithPattern() throws ParseException {
        Date date = new Date(116, 10, 1, 12, 23, 44);
        Date resultDate = DateUtil.parseDate(DateUtil.PATTERN_DEFAULT, "2016-11-01 12:23:44.000");
        assertThat(resultDate.getTime() == date.getTime()).isTrue();
    }

    @Test
    public void formatDuration() {
        assertThat(DateUtil.formatDuration(100)).isEqualTo("00:00:00.100");

        assertThat(DateUtil.formatDuration(new Date(100), new Date(3000))).isEqualTo("00:00:02.900");

        assertThat(DateUtil.formatDuration(DateUtil.MILLIS_PER_DAY * 2 + DateUtil.MILLIS_PER_HOUR * 4))
                .isEqualTo("52:00:00.000");

        assertThat(DateUtil.formatDurationOnSecond(new Date(100), new Date(3000))).isEqualTo("00:00:02");

        assertThat(DateUtil.formatDurationOnSecond(2000)).isEqualTo("00:00:02");

        assertThat(DateUtil.formatDurationOnSecond(DateUtil.MILLIS_PER_DAY * 2 + DateUtil.MILLIS_PER_HOUR * 4))
                .isEqualTo("52:00:00");
    }

    @Test
    public void formatFriendlyTimeSpanByNow() throws ParseException {
        try {
            Date now = DateUtil.DEFAULT_ON_SECOND_FORMAT.parse("2016-12-11 23:30:00");

            ClockUtil.useDummyClock(now);

            Date lessOneSecond = DateUtil.DEFAULT_FORMAT.parse("2016-12-11 23:29:59.500");
            assertThat(DateUtil.formatFriendlyTimeSpanByNow(lessOneSecond)).isEqualTo("刚刚");

            Date lessOneMinute = DateUtil.DEFAULT_FORMAT.parse("2016-12-11 23:29:55.000");
            assertThat(DateUtil.formatFriendlyTimeSpanByNow(lessOneMinute)).isEqualTo("5秒前");

            Date lessOneHour = DateUtil.DEFAULT_ON_SECOND_FORMAT.parse("2016-12-11 23:00:00");
            assertThat(DateUtil.formatFriendlyTimeSpanByNow(lessOneHour)).isEqualTo("30分钟前");

            Date today = DateUtil.DEFAULT_ON_SECOND_FORMAT.parse("2016-12-11 1:00:00");
            assertThat(DateUtil.formatFriendlyTimeSpanByNow(today)).isEqualTo("今天01:00");

            Date yesterday = DateUtil.DEFAULT_ON_SECOND_FORMAT.parse("2016-12-10 1:00:00");
            assertThat(DateUtil.formatFriendlyTimeSpanByNow(yesterday)).isEqualTo("昨天01:00");

            Date threeDayBefore = DateUtil.DEFAULT_ON_SECOND_FORMAT.parse("2016-12-09 1:00:00");
            assertThat(DateUtil.formatFriendlyTimeSpanByNow(threeDayBefore)).isEqualTo("2016-12-09");
        } finally {
            ClockUtil.useDefaultClock();
        }
    }
}