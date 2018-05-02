package com.pospi.util;

import java.math.BigDecimal;

/**
 * 保留两位小数
 */
public class DoubleSave {
    public static double doubleSaveTwo(double q) {
        BigDecimal b = new BigDecimal(q);
        double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return f1;
    }
}
