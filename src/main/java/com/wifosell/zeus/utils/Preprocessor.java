package com.wifosell.zeus.utils;

import java.util.List;

public class Preprocessor {
    // [] and [true, false,..., any] --> null
    // [true, true,..., true] --> true
    // [false, false,..., false] --> false
    public static Boolean convertToIsActive(List<Boolean> actives) {
        if (actives == null || actives.size() == 0)
            return null;
        if (actives.size() == 1 || !actives.stream().reduce((a, b) -> a ^ b).get())
            return actives.get(0);
        return null;
    }
}
