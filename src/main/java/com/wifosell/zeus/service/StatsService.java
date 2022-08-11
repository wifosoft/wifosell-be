package com.wifosell.zeus.service;

import com.wifosell.zeus.model.stats.LatestRevenues;
import com.wifosell.zeus.model.stats.TopOrderNumRegion;
import com.wifosell.zeus.model.stats.TopRevenueRegion;
import com.wifosell.zeus.model.stats.TopRevenueVariant;
import com.wifosell.zeus.utils.paging.PageInfo;

import java.math.BigDecimal;
import java.util.List;

public interface StatsService {
    PageInfo<TopRevenueVariant> getTopRevenueVariants(Long userId, Long startTime, Long endTime, Integer offset, Integer limit, String orderBy);

    BigDecimal getRevenue(Long userId, Long startTime, Long endTime);

    BigDecimal getProfit(Long userId, Long startTime, Long endTime);

    LatestRevenues getLatestRevenues(Long userId, Long lastTime, Integer number, LatestRevenues.Type type);

    Long getNumberOfOrders(Long userId, Long startTime, Long endTime, List<Boolean> isComplete, List<Boolean> isCanceled);

    PageInfo<TopRevenueRegion> getTopRevenueRegions(Long userId, Long startTime, Long endTime, Integer offset, Integer limit, String orderBy);

    PageInfo<TopOrderNumRegion> getTopOrderNumRegions(Long userId, Long startTime, Long endTime, Integer offset, Integer limit, String orderBy);
}
