package com.wifosell.zeus.utils;

import java.util.List;

public class Preprocessor {
    // [] and [true, false,..., any] --> null
    // [true, true,..., true] --> true
    // [false, false,..., false] --> false
    public static Boolean convertToIsActive(List<Boolean> actives) {
        Boolean isActive = null;
        if (actives.size() > 0 && !actives.stream().reduce((a, b) -> a ^ b).get())
            isActive = actives.get(0);
        return isActive;
    }
}
