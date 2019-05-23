package org.fuelteam.watt.lucky.number;

import java.math.BigDecimal;

public final class BigDecimalWrapper {

    private static final int ZERO = 0;

    private final BigDecimal bigDecimal;

    public BigDecimalWrapper(BigDecimal bigDecimal) {
        this.bigDecimal = bigDecimal;
    }

    public boolean eq(BigDecimal decimal) {
        return bigDecimal.compareTo(decimal) == ZERO;
    }

    public boolean eq(double decimal) {
        return eq(BigDecimal.valueOf(decimal));
    }

    public boolean gt(BigDecimal decimal) {
        return bigDecimal.compareTo(decimal) > ZERO;
    }

    public boolean gt(double decimal) {
        return gt(BigDecimal.valueOf(decimal));
    }

    public boolean gte(BigDecimal decimal) {
        return bigDecimal.compareTo(decimal) >= ZERO;
    }

    public boolean gte(double decimal) {
        return gte(BigDecimal.valueOf(decimal));
    }

    public boolean lt(BigDecimal decimal) {
        return bigDecimal.compareTo(decimal) < ZERO;
    }

    public boolean lt(double decimal) {
        return lt(BigDecimal.valueOf(decimal));
    }

    public boolean lte(BigDecimal decimal) {
        return bigDecimal.compareTo(decimal) <= ZERO;
    }

    public boolean lte(double decimal) {
        return lte(BigDecimal.valueOf(decimal));
    }

    public boolean notEq(BigDecimal decimal) {
        return !eq(decimal);
    }

    public boolean notEq(double decimal) {
        return !eq(decimal);
    }

    public boolean notGt(BigDecimal decimal) {
        return !gt(decimal);
    }

    public boolean notGt(double decimal) {
        return !gt(decimal);
    }

    public boolean notGte(BigDecimal decimal) {
        return !gte(decimal);
    }

    public boolean notGte(double decimal) {
        return !gte(decimal);
    }

    public boolean notLt(BigDecimal decimal) {
        return !lt(decimal);
    }

    public boolean notLt(double decimal) {
        return !lt(decimal);
    }

    public boolean notLte(BigDecimal decimal) {
        return !lte(decimal);
    }

    public boolean notLte(double decimal) {
        return !lte(decimal);
    }

    public boolean isPositive() {
        return gt(ZERO);
    }

    public boolean isNegative() {
        return lt(ZERO);
    }

    public boolean isNonPositive() {
        return lte(ZERO);
    }

    public boolean isNonNegative() {
        return gte(ZERO);
    }

    public boolean isZero() {
        return eq(ZERO);
    }

    public boolean isNotZero() {
        return notEq(ZERO);
    }

    public boolean isNullOrZero() {
        return bigDecimal == null || isZero();
    }

    public boolean notNullOrZero() {
        return bigDecimal != null && isNotZero();
    }
}