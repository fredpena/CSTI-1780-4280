package com.pucmm.csti.utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class FormattedUtil {

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");//= new SimpleDateFormat(ConfigurationJS.getProperty("format.date"));
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,##0.00");//= new DecimalFormat(ConfigurationJS.getProperty("format.decimal"));

    private FormattedUtil() {
    }

    public static SimpleDateFormat getSimpleDateFormat() {
        return SIMPLE_DATE_FORMAT;
    }

    public static DecimalFormat getDecimalFormat() {
        return DECIMAL_FORMAT;
    }

    public static String getDecimalValue(double value) {
        return DECIMAL_FORMAT.format(value);
    }

    public static Double getDecimalValue(String value) {
        return Double.valueOf(value.replace(",", "").trim());
    }

    public static Double getDecimalRound(Double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
