package com.wifosell.zeus.utils;

public class NumberUtils {
    public static int tryParseInt(String value , int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch(NumberFormatException nfe) {
            // Log exception.
            return defaultValue;
        }
    }
}
