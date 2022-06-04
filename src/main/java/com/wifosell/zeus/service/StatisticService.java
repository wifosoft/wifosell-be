package com.wifosell.zeus.service;

import com.wifosell.zeus.model.product.Product;
import com.wifosell.zeus.payload.response.statistic.TopSellerProductResponse;

import java.time.Instant;
import java.util.*;

public interface StatisticService {

    List<TopSellerProductResponse> topSeller(Instant dateFrom, Instant dateTo, Integer limit, Integer offset, Integer top);
    List<TopSellerProductResponse> topSellerByShopId(Long shopId, Instant dateFrom, Instant dateTo, Integer limit, Integer offset, Integer top);
    Long grossRevenue(Instant dateFrom, Instant dateTo);
    Long grossRevenueByShopId(Long shopId, Instant dateFrom, Instant dateTo);
    Long revenuePerEmployee(Long userId, Date from, Date to);
    Long revenuePerEmployeeByShopId(Long userId, Long shopId, Date from, Date to);
    Long totalOrder(Instant dateFrom, Instant dateTo);
    Long totalOrderByShopId(Long shopId, Instant from, Instant to);
}
