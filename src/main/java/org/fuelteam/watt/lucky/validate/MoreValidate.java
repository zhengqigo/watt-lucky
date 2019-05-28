package org.fuelteam.watt.lucky.validate;

import org.fuelteam.watt.lucky.annotation.Nullable;

public class MoreValidate extends org.apache.commons.lang3.Validate {

    public static int positive(@Nullable String role, int x) {
        if (x <= 0) {
            String message = "%s (%s) must be > 0";
            throw new IllegalArgumentException(String.format(message, role, x));
        }
        return x;
    }

    public static Integer positive(@Nullable String role, Integer x) {
        if (x.intValue() <= 0) {
            String message = "%s (%s) must be > 0";
            throw new IllegalArgumentException(String.format(message, role, x));
        }
        return x;
    }

    public static long positive(@Nullable String role, long x) {
        if (x <= 0) {
            String message = "%s (%s) must be > 0";
            throw new IllegalArgumentException(String.format(message, role, x));
        }
        return x;
    }

    public static Long positive(@Nullable String role, Long x) {
        if (x.longValue() <= 0) {
            String message = "%s (%s) must be > 0";
            throw new IllegalArgumentException(String.format(message, role, x));
        }
        return x;
    }

    public static double positive(@Nullable String role, double x) {
        if (!(x > 0)) { // not x < 0, to work with NaN.
            String message = "%s (%s) must be >= 0";
            throw new IllegalArgumentException(String.format(message, role, x));
        }
        return x;
    }

    public static int nonNegative(@Nullable String role, int x) {
        if (x < 0) {
            String message = "%s (%s) must be >= 0";
            throw new IllegalArgumentException(String.format(message, role, x));
        }
        return x;
    }

    public static Integer nonNegative(@Nullable String role, Integer x) {
        if (x.intValue() < 0) {
            String message = "%s (%s) must be >= 0";
            throw new IllegalArgumentException(String.format(message, role, x));
        }
        return x;
    }

    public static long nonNegative(@Nullable String role, long x) {
        if (x < 0) {
            String message = "%s (%s) must be >= 0";
            throw new IllegalArgumentException(String.format(message, role, x));
        }
        return x;
    }

    public static Long nonNegative(@Nullable String role, Long x) {
        if (x.longValue() < 0) {
            String message = "%s (%s) must be >= 0";
            throw new IllegalArgumentException(String.format(message, role, x));
        }
        return x;
    }

    public static double nonNegative(@Nullable String role, double x) {
        if (!(x >= 0)) { // not x < 0, to work with NaN
            String message = "%s (%s) must be >= 0";
            throw new IllegalArgumentException(String.format(message, role, x));
        }
        return x;
    }
}