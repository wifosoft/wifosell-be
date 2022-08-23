package com.wifosell.zeus.model.stats;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class LatestRevenues {
    private final Type type;
    private final List<Item> items;

    public enum Type {
        DAY,
        WEEK,
        MONTH,
        YEAR
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Item {
        private final Long time;
        private final BigDecimal revenue;
    }
}
