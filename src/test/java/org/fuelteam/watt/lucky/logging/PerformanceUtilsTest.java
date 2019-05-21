package org.fuelteam.watt.lucky.logging;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PerformanceUtilsTest {

    private static final Logger logger = (Logger) LoggerFactory.getLogger(PerformanceUtilsTest.class);

    @Test
    public void test() throws InterruptedException {
        PerformanceUtil.start();
        PerformanceUtil.start("test");
        Thread.sleep(1000L);// NOSONAR

        PerformanceUtil.endWithSlowLog(logger, 100L);
        PerformanceUtil.endWithSlowLog(logger, "test", 100L);
    }
}