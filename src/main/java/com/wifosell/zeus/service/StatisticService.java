package com.wifosell.zeus.service;

import com.wifosell.zeus.model.product.Product;
import com.wifosell.zeus.payload.response.statistic.TopSellerProductResponse;

import java.time.Instant;
import java.util.*;

public interface StatisticService {

    List<TopSellerProductResponse> topSeller(Instant dateFrom, Instant dateTo, int limit, int offset, int top);
    List<Product> topSellerByShopId(Long shopId, Instant dateFrom, Instant dateTo, int limit, int offset, int top);
    Long grossRevenue(Instant dateFrom, Instant dateTo);
    Long grossRevenueByShopId(Long shopId, Date from, Date to);
    Long revenuePerEmployee(Long userId, Date from, Date to);
    Long revenuePerEmployeeByShopId(Long userId, Long shopId, Date from, Date to);
    Long totalOrder(Instant dateFrom, Instant dateTo);
    Long totalOrderByShopId(Long shopId, Instant from, Instant to);
}
