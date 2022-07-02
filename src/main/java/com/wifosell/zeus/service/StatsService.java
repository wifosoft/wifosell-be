package com.wifosell.zeus.service;

import com.wifosell.zeus.model.stats.TopRevenueVariant;
import com.wifosell.zeus.utils.paging.PageInfo;

public interface StatsService {
    PageInfo<TopRevenueVariant> getTopRevenueVariants(Long userId, Long fromDate, Long toDate, Integer offset, Integer limit, String orderBy);
}
