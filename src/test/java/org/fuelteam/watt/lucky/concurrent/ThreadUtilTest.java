package org.fuelteam.watt.lucky.concurrent;

import static org.assertj.core.api.Assertions.assertThat;

import org.fuelteam.watt.lucky.utils.ObjectUtil;
import org.fuelteam.watt.lucky.utils.RuntimeUtil;
import org.junit.Test;

public class ThreadUtilTest {
    @Test
    public void testCaller() {
        hello();
        new MyClass().hello();
        assertThat(RuntimeUtil.getCurrentClass()).isEqualTo("com.vip.vjtools.vjkit.concurrent.ThreadUtilTest");
        assertThat(RuntimeUtil.getCurrentMethod()).isEqualTo("com.vip.vjtools.vjkit.concurrent.ThreadUtilTest.testCaller()");
    }

    private void hello() {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        System.out.println(ObjectUtil.toPrettyString(stacktrace));

        assertThat(RuntimeUtil.getCallerClass()).isEqualTo("com.vip.vjtools.vjkit.concurrent.ThreadUtilTest");
        assertThat(RuntimeUtil.getCallerMethod()).isEqualTo("com.vip.vjtools.vjkit.concurrent.ThreadUtilTest.testCaller()");
    }

    public static class MyClass {
        public void hello() {
            StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
            System.out.println(ObjectUtil.toPrettyString(stacktrace));

            assertThat(RuntimeUtil.getCallerClass()).isEqualTo("com.vip.vjtools.vjkit.concurrent.ThreadUtilTest");
            assertThat(RuntimeUtil.getCallerMethod()).isEqualTo("com.vip.vjtools.vjkit.concurrent.ThreadUtilTest.testCaller()");
        }
    }
}