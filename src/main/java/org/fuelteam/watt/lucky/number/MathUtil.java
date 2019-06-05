package org.fuelteam.watt.lucky.number;

import java.math.RoundingMode;

import com.google.common.math.IntMath;
import com.google.common.math.LongMath;

public class MathUtil {

    public static int nextPowerOfTwo(int value) {
        return IntMath.ceilingPowerOfTwo(value);
    }

    public static long nextPowerOfTwo(long value) {
        return LongMath.ceilingPowerOfTwo(value);
    }

    public static int previousPowerOfTwo(int value) {
        return IntMath.floorPowerOfTwo(value);
    }

    public static long previousPowerOfTwo(long value) {
        return LongMath.floorPowerOfTwo(value);
    }

    public static boolean isPowerOfTwo(int value) {
        return IntMath.isPowerOfTwo(value);
    }

    public static boolean isPowerOfTwo(long value) {
        return LongMath.isPowerOfTwo(value);
    }

    public static int modByPowerOfTwo(int value, int mod) {
        return value & (mod - 1);
    }

    public static int mod(int x, int m) {
        return IntMath.mod(x, m);
    }

    public static long mod(long x, long m) {
        return LongMath.mod(x, m);
    }

    public static int mod(long x, int m) {
        return LongMath.mod(x, m);
    }

    public static int divide(int p, int q, RoundingMode mode) {
        return IntMath.divide(p, q, mode);
    }

    public static long divide(long p, long q, RoundingMode mode) {
        return LongMath.divide(p, q, mode);
    }

    public static int pow(int b, int k) {
        return IntMath.pow(b, k);
    }

    public static long pow(long b, int k) {
        return LongMath.pow(b, k);
    }

    public static int sqrt(int x, RoundingMode mode) {
        return IntMath.sqrt(x, mode);
    }

    public static long sqrt(long x, RoundingMode mode) {
        return LongMath.sqrt(x, mode);
    }
}