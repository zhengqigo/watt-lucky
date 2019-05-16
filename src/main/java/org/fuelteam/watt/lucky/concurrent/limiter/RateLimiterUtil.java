package org.fuelteam.watt.lucky.concurrent.limiter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.google.common.util.concurrent.RateLimiter;

public class RateLimiterUtil {

    /**
     * 定制RateLimiter，默认开始桶里装满token
     * 
     * @param permitsPerSecond 每秒允许的请求数QPS
     * @param maxBurstSeconds 桶容量，Guava最大的突发流量缓冲时间默认是1s，permitsPerSecond * maxBurstSeconds是闲置时累积的缓冲token最大值
     */
    public static RateLimiter create(double permitsPerSecond, double maxBurstSeconds) throws ReflectiveOperationException {
        return create(permitsPerSecond, maxBurstSeconds, true);
    }

    /**
     * 定制RateLimiter
     * 
     * @param permitsPerSecond 每秒允许的请求数QPS
     * @param maxBurstSeconds 最大的突发缓冲时间，用来应对突发流量，Guava实现默认是1s，permitsPerSecond * maxBurstSeconds的数量，就是闲置时预留的缓冲token数量
     * @param filledWithToken 是否需要创建时就保留有permitsPerSecond * maxBurstSeconds的token
     */
    public static RateLimiter create(double permitsPerSecond, double maxBurstSeconds, boolean filledWithToken)
            throws ReflectiveOperationException {
        Class<?> sleepingStopwatchClass = Class.forName("com.google.common.util.concurrent.RateLimiter$SleepingStopwatch");
        Method createStopwatchMethod = sleepingStopwatchClass.getDeclaredMethod("createFromSystemTimer");
        createStopwatchMethod.setAccessible(true);
        Object stopwatch = createStopwatchMethod.invoke(null);

        Class<?> burstyRateLimiterClass = Class.forName("com.google.common.util.concurrent.SmoothRateLimiter$SmoothBursty");
        Constructor<?> burstyRateLimiterConstructor = burstyRateLimiterClass.getDeclaredConstructors()[0];
        burstyRateLimiterConstructor.setAccessible(true);

        RateLimiter rateLimiter = (RateLimiter) burstyRateLimiterConstructor.newInstance(stopwatch, maxBurstSeconds);
        rateLimiter.setRate(permitsPerSecond);

        if (filledWithToken) {
            setField(rateLimiter, "storedPermits", permitsPerSecond * maxBurstSeconds);
        }

        return rateLimiter;
    }

    private static boolean setField(Object targetObject, String fieldName, Object fieldValue) {
        Field field;
        try {
            field = targetObject.getClass().getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            field = null;
        }
        Class<?> superClass = targetObject.getClass().getSuperclass();
        while (field == null && superClass != null) {
            try {
                field = superClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                superClass = superClass.getSuperclass();
            }
        }
        if (field == null) return false;
        field.setAccessible(true);
        try {
            field.set(targetObject, fieldValue);
            return true;
        } catch (IllegalAccessException e) {
            return false;
        }
    }
}