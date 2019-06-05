package org.fuelteam.watt.lucky.number;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;

import org.apache.commons.lang3.StringUtils;

public class MoneyUtil {

    private static final ThreadLocal<DecimalFormat> DEFAULT_FORMAT = createThreadLocalNumberformat("0.00");

    private static final ThreadLocal<DecimalFormat> PRETTY_FORMAT = createThreadLocalNumberformat("#,##0.00");

    // ThreadLocal重用MessageDigest
    private static ThreadLocal<DecimalFormat> createThreadLocalNumberformat(final String pattern) {
        return new ThreadLocal<DecimalFormat>() {
            @Override
            protected DecimalFormat initialValue() {
                DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance();
                df.applyPattern(pattern);
                return df;
            }
        };
    }

    public static BigDecimal fen2yuan(BigDecimal num) {
        return num.divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
    }

    public static BigDecimal fen2yuan(long num) {
        return fen2yuan(new BigDecimal(num));
    }

    public static BigDecimal fen2yuan(String num) {
        if (StringUtils.isEmpty(num)) return new BigDecimal(0);
        return fen2yuan(new BigDecimal(num));
    }

    public static BigDecimal yuan2fen(String y) {
        return new BigDecimal(Math.round(new BigDecimal(y).multiply(new BigDecimal(100)).doubleValue()));
    }

    public static BigDecimal yuan2fen(double y) {
        return yuan2fen(String.valueOf(y));
    }

    public static BigDecimal yuan2fen(BigDecimal y) {
        if (y != null) return yuan2fen(y.toString());
        return new BigDecimal(0);
    }

    public static String format(BigDecimal number) {
        return format(number.doubleValue());
    }

    public static String format(double number) {
        return DEFAULT_FORMAT.get().format(number);
    }

    public static String prettyFormat(BigDecimal number) {
        return prettyFormat(number.doubleValue());
    }

    public static String prettyFormat(double number) {
        return PRETTY_FORMAT.get().format(number);
    }

    public static String format(BigDecimal number, String pattern) {
        return format(number.doubleValue(), pattern);
    }

    public static String format(double number, String pattern) {
        DecimalFormat df = null;
        if (StringUtils.isEmpty(pattern)) {
            df = PRETTY_FORMAT.get();
        } else {
            df = (DecimalFormat) DecimalFormat.getInstance();
            df.applyPattern(pattern);
        }
        return df.format(number);
    }

    public static BigDecimal parseString(String numberStr) throws ParseException {
        return new BigDecimal(DEFAULT_FORMAT.get().parse(numberStr).doubleValue());
    }

    public static BigDecimal parsePrettyString(String numberStr) throws ParseException {
        return new BigDecimal(PRETTY_FORMAT.get().parse(numberStr).doubleValue());
    }

    public static BigDecimal parseString(String numberStr, String pattern) throws ParseException {
        DecimalFormat df = null;
        if (StringUtils.isEmpty(pattern)) {
            df = PRETTY_FORMAT.get();
        } else {
            df = (DecimalFormat) DecimalFormat.getInstance();
            df.applyPattern(pattern);
        }
        return new BigDecimal(df.parse(numberStr).doubleValue());
    }
}