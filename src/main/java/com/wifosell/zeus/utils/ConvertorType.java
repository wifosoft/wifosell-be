package com.wifosell.zeus.utils;

import java.util.Arrays;

public class ConvertorType {
    public static String[] getNames(Class<? extends Enum<?>> e) {
        return Arrays.stream(e.getEnumConstants()).map(Enum::name).toArray(String[]::new);
    }
}
