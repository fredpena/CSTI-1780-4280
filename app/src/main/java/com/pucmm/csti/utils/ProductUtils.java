package com.pucmm.csti.utils;

public class ProductUtils {

    private static final int COUNT = 5;
    private static final String WITH = "O";

    public static String completeRight(int value) {
        int sCount = COUNT - String.valueOf(value).length();
        return value + replicate(WITH, sCount);
    }

    private static String replicate(String str, int count) {
        String newString = "I";
        for (int i = 0; i < count; i++) {
            newString += str;
        }
        return newString;
    }

    public static String completeLeft(int value) {
        int sCount = COUNT - String.valueOf(value).length();
        return replicate(WITH, sCount) + value;
    }
}
