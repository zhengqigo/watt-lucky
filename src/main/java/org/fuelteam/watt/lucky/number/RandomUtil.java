package org.fuelteam.watt.lucky.number;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.Validate;
import org.fuelteam.watt.lucky.validate.MoreValidate;

public class RandomUtil {

    public static Random threadLocalRandom() {
        return ThreadLocalRandom.current();
    }

    /**
     * @see SecureRandom的江湖偏方与真实效果http://calvin1978.blogcn.com/articles/securerandom.html
     */
    public static SecureRandom secureRandom() {
        try {
            return SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {// NOSONAR
            return new SecureRandom();
        }
    }

    public static int nextInt() {
        return nextInt(ThreadLocalRandom.current());
    }

    public static int nextInt(Random random) {
        int n = random.nextInt();
        if (n == Integer.MIN_VALUE) {
            n = 0; // corner case
        } else {
            n = Math.abs(n);
        }
        return n;
    }

    public static int nextInt(int max) {
        return nextInt(ThreadLocalRandom.current(), max);
    }

    public static int nextInt(Random random, int max) {
        return random.nextInt(max);
    }

    public static int nextInt(int min, int max) {
        return nextInt(ThreadLocalRandom.current(), min, max);
    }

    public static int nextInt(Random random, int min, int max) {
        Validate.isTrue(max >= min, "Start value must be smaller or equal to end value.");
        MoreValidate.nonNegative("min", min);
        if (min == max) return min;
        return min + random.nextInt(max - min);
    }

    public static long nextLong() {
        return nextLong(ThreadLocalRandom.current());
    }

    public static long nextLong(Random random) {
        long n = random.nextLong();
        if (n == Long.MIN_VALUE) {
            n = 0; // corner case
        } else {
            n = Math.abs(n);
        }
        return n;
    }

    public static long nextLong(long max) {
        return nextLong(ThreadLocalRandom.current(), 0, max);
    }

    public static long nextLong(Random random, long max) {
        return nextLong(random, 0, max);
    }

    public static long nextLong(long min, long max) {
        return nextLong(ThreadLocalRandom.current(), min, max);
    }

    public static long nextLong(Random random, long min, long max) {
        Validate.isTrue(max >= min, "Start value must be smaller or equal to end value.");
        MoreValidate.nonNegative("min", min);
        if (min == max) return min;
        return (long) (min + ((max - min) * random.nextDouble()));
    }

    public static double nextDouble() {
        return nextDouble(ThreadLocalRandom.current(), 0, Double.MAX_VALUE);
    }

    public static double nextDouble(Random random) {
        return nextDouble(random, 0, Double.MAX_VALUE);
    }

    public static double nextDouble(double max) {
        return nextDouble(ThreadLocalRandom.current(), 0, max);
    }

    public static double nextDouble(Random random, double max) {
        return nextDouble(random, 0, max);
    }

    public static double nextDouble(final double min, final double max) {
        return nextDouble(ThreadLocalRandom.current(), min, max);
    }

    public static double nextDouble(Random random, final double min, final double max) {
        Validate.isTrue(max >= min, "Start value must be smaller or equal to end value.");
        MoreValidate.nonNegative("min", min);
        if (Double.compare(min, max) == 0) return min;
        return min + ((max - min) * random.nextDouble());
    }

    public static String randomStringFixLength(int length) {
        return RandomStringUtils.random(length, 0, 0, true, true, null, threadLocalRandom());
    }

    public static String randomStringFixLength(Random random, int length) {
        return RandomStringUtils.random(length, 0, 0, true, true, null, random);
    }

    public static String randomStringRandomLength(int minLength, int maxLength) {
        return RandomStringUtils.random(nextInt(minLength, maxLength), 0, 0, true, true, null, threadLocalRandom());
    }

    public static String randomStringRandomLength(Random random, int minLength, int maxLength) {
        return RandomStringUtils.random(nextInt(random, minLength, maxLength), 0, 0, true, true, null, random);
    }

    public static String randomLetterFixLength(int length) {
        return RandomStringUtils.random(length, 0, 0, true, false, null, threadLocalRandom());
    }

    public static String randomLetterFixLength(Random random, int length) {
        return RandomStringUtils.random(length, 0, 0, true, false, null, random);
    }

    public static String randomLetterRandomLength(int minLength, int maxLength) {
        return RandomStringUtils.random(nextInt(minLength, maxLength), 0, 0, true, false, null, threadLocalRandom());
    }

    public static String randomLetterRandomLength(Random random, int minLength, int maxLength) {
        return RandomStringUtils.random(nextInt(random, minLength, maxLength), 0, 0, true, false, null, random);
    }

    public static String randomAsciiFixLength(int length) {
        return RandomStringUtils.random(length, 32, 127, false, false, null, threadLocalRandom());
    }

    public static String randomAsciiFixLength(Random random, int length) {
        return RandomStringUtils.random(length, 32, 127, false, false, null, random);
    }

    public static String randomAsciiRandomLength(int minLength, int maxLength) {
        return RandomStringUtils.random(nextInt(minLength, maxLength), 32, 127, false, false, null, threadLocalRandom());
    }

    public static String randomAsciiRandomLength(Random random, int minLength, int maxLength) {
        return RandomStringUtils.random(nextInt(random, minLength, maxLength), 32, 127, false, false, null, random);
    }
}