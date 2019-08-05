package io.costax;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Objects;

import static java.math.BigDecimal.ZERO;

public final class BigDecimals {

    /**
     * Hundred (100.0)
     */
    public static final BigDecimal HUNDRED = new BigDecimal("100.0");
    /**
     * Hundredth (0.01)
     * ~ or, simpler ~
     * zero point zero one
     */
    public static final BigDecimal ZERO_POINT_ZERO_ONE = new BigDecimal("0.01");
    private static DecimalFormat DECIMAL_FORMAT;

    private BigDecimals() {
        throw new AssertionError();
    }

    private static final DecimalFormat getDecimalFormatWithoutRounding() {
        if (DECIMAL_FORMAT == null) {
            synchronized (BigDecimals.class) {
                if (DECIMAL_FORMAT == null) {
                    DECIMAL_FORMAT = new DecimalFormat("###0.00");
                    DECIMAL_FORMAT.setRoundingMode(RoundingMode.DOWN);
                }
            }
        }

        return DECIMAL_FORMAT;
    }

    public static String formatWithoutRounding(final BigDecimal value) {
        return getDecimalFormatWithoutRounding().format(value);
    }

    public static boolean isDifferenceIsGreaterThan(BigDecimal a, BigDecimal b, double acceptedDifference) {
        Objects.requireNonNull(a);
        Objects.requireNonNull(b);
        return a.subtract(b).abs().compareTo(BigDecimal.valueOf(acceptedDifference)) > 0;
    }

    public static boolean isGreaterThanZero(BigDecimal value) {
        return isGreaterThan(value, ZERO);
    }

    public static boolean isGreaterOrEqualThanZero(BigDecimal value) {
        return isGreaterOrEqualThan(value, ZERO);
    }

    public static boolean isLessThanZero(BigDecimal value) {
        return isLessThan(value, ZERO);
    }

    public static boolean isLessOrEqualThanZero(BigDecimal value) {
        return isLessOrEqualThan(value, ZERO);
    }

    public static boolean isEqualThanZero(BigDecimal value) {
        return isEqualThan(value, ZERO);
    }

    public static boolean isGreaterThan(BigDecimal value, BigDecimal other) {
        Objects.requireNonNull(value);
        Objects.requireNonNull(other);
        return value.compareTo(other) > 0;
    }

    public static boolean isGreaterOrEqualThan(BigDecimal value, BigDecimal other) {
        Objects.requireNonNull(value);
        Objects.requireNonNull(other);
        return value.compareTo(other) >= 0;
    }

    public static boolean isLessThan(BigDecimal value, BigDecimal other) {
        Objects.requireNonNull(value);
        Objects.requireNonNull(other);
        return value.compareTo(other) < 0;
    }

    public static boolean isLessOrEqualThan(BigDecimal value, BigDecimal other) {
        Objects.requireNonNull(value);
        Objects.requireNonNull(other);
        return value.compareTo(other) <= 0;
    }

    public static boolean isEqualThan(BigDecimal value, BigDecimal other) {
        Objects.requireNonNull(value);
        return value.compareTo(other) == 0;
    }

    public static boolean isGreaterThan(BigDecimal value, double other) {
        return isGreaterThan(value, BigDecimal.valueOf(other));
    }

    public static boolean isGreaterOrEqualThan(BigDecimal value, double other) {
        return isGreaterOrEqualThan(value, BigDecimal.valueOf(other));
    }

    public static boolean isLessThan(BigDecimal value, double other) {
        return isLessThan(value, BigDecimal.valueOf(other));
    }

    public static boolean isLessOrEqualThan(BigDecimal value, double other) {
        return isLessOrEqualThan(value, BigDecimal.valueOf(other));
    }

    public static boolean isEqualThan(BigDecimal value, double other) {
        return isEqualThan(value, BigDecimal.valueOf(other));
    }

    public static boolean isEqualThan100(BigDecimal value) {
        return value != null && isEqualThan(value, HUNDRED);
    }

    public static BigDecimal divideBy100(BigDecimal value) {
        return value.multiply(ZERO_POINT_ZERO_ONE);
    }

    public static boolean isNullOrEqualsToZero(final BigDecimal value) {
        return value == null || isEqualThanZero(value);
    }
}
