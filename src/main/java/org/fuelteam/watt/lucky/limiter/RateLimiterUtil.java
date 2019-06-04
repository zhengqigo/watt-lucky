package org.fuelteam.watt.lucky.limiter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.google.common.util.concurrent.RateLimiter;

public class RateLimiterUtil {

    public static RateLimiter create(double permitsPerSecond, double maxBurstSeconds) throws ReflectiveOperationException {
        return create(permitsPerSecond, maxBurstSeconds, true);
    }

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

        if (filledWithToken) setField(rateLimiter, "storedPermits", permitsPerSecond * maxBurstSeconds);

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