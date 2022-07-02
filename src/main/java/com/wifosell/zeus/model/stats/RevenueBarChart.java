package com.wifosell.zeus.model.stats;

import com.wifosell.zeus.utils.paging.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class RevenueBarChart {
    private final Type type;
    private final PageInfo<Item> items;

    public enum Type {
        DAY,
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
