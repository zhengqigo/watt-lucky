package org.fuelteam.watt.lucky.concurrent;

import java.util.concurrent.TimeUnit;

/**
 * 线程相关工具类
 */
public class ThreadUtil {

    /**
     * sleep毫秒，已捕捉并处理InterruptedException
     */
    public static void sleep(long durationMillis) {
        try {
            Thread.sleep(durationMillis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * sleep，已捕捉并处理InterruptedException
     */
    public static void sleep(long duration, TimeUnit unit) {
        try {
            Thread.sleep(unit.toMillis(duration));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 处理InterruptedException的正确方式
     */
    public static void handleInterruptedException() {
        Thread.currentThread().interrupt();
    }
}