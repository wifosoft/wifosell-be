package com.wifosell.zeus.service;

import com.wifosell.zeus.model.stats.RevenueBarChart;
import com.wifosell.zeus.model.stats.TopRevenueVariant;
import com.wifosell.zeus.utils.paging.PageInfo;

import java.util.List;

public interface StatsService {
    PageInfo<TopRevenueVariant> getTopRevenueVariants(Long userId, Long fromDate, Long toDate, Integer offset, Integer limit, String orderBy);

    Long getRevenue(Long userId, Long fromDate, Long toDate);

    RevenueBarChart getRevenueBarChart(Long userId, Long fromDate, Long toDate, RevenueBarChart.Type type, Integer offset, Integer limit);

    Long getNumberOfOrders(Long userId, Long fromDate, Long toDate, List<Boolean> isComplete, List<Boolean> isCanceled);
}
