package com.utli;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalUtil {

    public static double add(double a, double b) {
        return BigDecimal.valueOf(a).add(BigDecimal.valueOf(b)).doubleValue();
    }

    public static double subtract(double a, double b) {
        return BigDecimal.valueOf(a).subtract(BigDecimal.valueOf(b)).doubleValue();
    }

    public static double multiply(double a, double b) {
        return BigDecimal.valueOf(a).multiply(BigDecimal.valueOf(b)).doubleValue();
    }

    public static double divide(double a, double b, int scale) {
        BigDecimal result = BigDecimal.valueOf(a)
                .divide(BigDecimal.valueOf(b), scale, RoundingMode.HALF_UP)
                .setScale(scale, RoundingMode.HALF_UP);
        return result.doubleValue();
    }
}