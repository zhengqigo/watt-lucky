package org.fuelteam.watt.lucky.time;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import org.junit.Test;

@SuppressWarnings("deprecation")
public class DateUtilTest {

    @Test
    public void getDayOfWeek() {
        Date date = new Date(117, 0, 9);
        assertThat(DateUtil.getDayOfWeek(date)).isEqualTo(1);

        Date date2 = new Date(117, 0, 15);
        assertThat(DateUtil.getDayOfWeek(date2)).isEqualTo(7);
    }

    @Test
    public void isLeapYear() {
        // 2008-01-09，整除4年，true
        Date date = new Date(108, 0, 9);
        assertThat(DateUtil.isLeapYear(date)).isTrue();

        // 2000-01-09，整除400年，true
        date = new Date(100, 0, 9);
        assertThat(DateUtil.isLeapYear(date)).isTrue();

        // 1900-01-09，整除100年，false
        date = new Date(0, 0, 9);
        assertThat(DateUtil.isLeapYear(date)).isFalse();
    }

    @Test
    public void getXXofXX() {
        // 2008-02-09，整除4年，闰年
        Date date = new Date(108, 2, 9);
        assertThat(DateUtil.getMonthLength(date)).isEqualTo(29);

        // 2009-02-09，整除4年，非闰年
        Date date2 = new Date(109, 2, 9);
        assertThat(DateUtil.getMonthLength(date2)).isEqualTo(28);

        Date date3 = new Date(108, 8, 9);
        assertThat(DateUtil.getMonthLength(date3)).isEqualTo(31);

        Date date4 = new Date(109, 11, 30);
        assertThat(DateUtil.getDayOfYear(date4)).isEqualTo(364);

        Date date5 = new Date(117, 0, 12);
        assertThat(DateUtil.getWeekOfMonth(date5)).isEqualTo(3);
        assertThat(DateUtil.getWeekOfYear(date5)).isEqualTo(3);
    }
}