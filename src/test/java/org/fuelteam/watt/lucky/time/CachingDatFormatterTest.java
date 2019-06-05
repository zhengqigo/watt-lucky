package org.fuelteam.watt.lucky.time;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import org.junit.Test;

public class CachingDatFormatterTest {

    @Test
    public void test() {
        Date date = DateUtil.str2date("2016-11-01 12:23:44.000", "yyyy-MM-dd HH:mm:ss.SSS");

        CachingDateFormatter formatter = new CachingDateFormatter(DateUtil.PATTERN_DEFAULT);
        assertThat(formatter.format(date.getTime())).isEqualTo("2016-11-01 12:23:44.000");
        assertThat(formatter.format(date.getTime())).isEqualTo("2016-11-01 12:23:44.000");
        assertThat(formatter.format(date.getTime() + 2)).isEqualTo("2016-11-01 12:23:44.002");

        CachingDateFormatter formatterOnSecond = new CachingDateFormatter(DateUtil.PATTERN_DEFAULT_ON_SECOND);
        assertThat(formatterOnSecond.format(date.getTime())).isEqualTo("2016-11-01 12:23:44");
        assertThat(formatterOnSecond.format(date.getTime())).isEqualTo("2016-11-01 12:23:44");
        assertThat(formatterOnSecond.format(date.getTime() + 2)).isEqualTo("2016-11-01 12:23:44");
        assertThat(formatterOnSecond.format(date.getTime() + 1000)).isEqualTo("2016-11-01 12:23:45");
    }
}