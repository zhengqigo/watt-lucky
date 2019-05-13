package org.fuelteam.watt.lucky.concurrent.limiter;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;

import org.fuelteam.watt.lucky.concurrent.Concurrents;
import org.fuelteam.watt.lucky.concurrent.limiter.TimeIntervalLimiter;
import org.junit.Test;

public class TimeIntervalLimiterTest {

    @Test
    public void testTryAcquire() throws Exception {
        int interval = 100;
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;

        TimeIntervalLimiter limiter = Concurrents.timeIntervalLimiter(interval, timeUnit);

        assertThat(limiter.tryAcquire()).isTrue();
        assertThat(limiter.tryAcquire()).isFalse();

        timeUnit.sleep(interval);
        assertThat(limiter.tryAcquire()).isTrue();
    }
}