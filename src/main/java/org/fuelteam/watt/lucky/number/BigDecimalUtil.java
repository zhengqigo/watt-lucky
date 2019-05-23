package org.fuelteam.watt.lucky.number;

import java.math.BigDecimal;

public class BigDecimalUtil {

    public static BigDecimalWrapper get(BigDecimal decimal) {
        return new BigDecimalWrapper(decimal);
    }

    public static BigDecimalWrapper get(double dbl) {
        return get(BigDecimal.valueOf(dbl));
    }
    
    public static <T extends Number> BigDecimal safeDivide(T b1, T b2, int points) {
        return safeDivide(b1, b2, points, BigDecimal.ZERO);
    }

    public static <T extends Number> BigDecimal safeDivide(T b1, T b2, int points, BigDecimal defVal) {
        if (null == b1 || null == b2) return defVal;
        try {
            return BigDecimal.valueOf(b1.doubleValue()).divide(BigDecimal.valueOf(b2.doubleValue()), points,
                    BigDecimal.ROUND_HALF_UP);
        } catch (Exception ex) {
            return defVal;
        }
    }

    public static <T extends Number> BigDecimal safeSubtract(T b1, T b2) {
        return safeSubtract(b1, b2, BigDecimal.ZERO);
    }

    public static <T extends Number> BigDecimal safeSubtract(T b1, T b2, BigDecimal defVal) {
        if (null == b1 || null == b2) return defVal;
        try {
            return BigDecimal.valueOf(b1.doubleValue()).subtract(BigDecimal.valueOf(b2.doubleValue()));
        } catch (Exception ex) {
            return defVal;
        }
    }
}