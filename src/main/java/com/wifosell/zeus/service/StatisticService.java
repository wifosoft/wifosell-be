package com.wifosell.zeus.service;

import com.wifosell.zeus.model.product.Product;
import com.wifosell.zeus.payload.response.statistic.TopRevenueEmployeeResponse;
import com.wifosell.zeus.payload.response.statistic.TopSellerProductResponse;

import java.time.Instant;
import java.util.Date;
import java.util.List;

public interface StatisticService {

    List<TopSellerProductResponse> topSeller(Instant dateFrom, Instant dateTo);
    List<TopSellerProductResponse> topSellerByShopId(Long shopId, Instant dateFrom, Instant dateTo);
    List<TopRevenueEmployeeResponse> topEmployee(Instant dateFrom, Instant dateTo);
    List<TopRevenueEmployeeResponse> topEmployeeByShopId(Long shopId, Instant dateFrom, Instant dateTo);
    Long grossRevenue(Instant dateFrom, Instant dateTo);
    Long grossRevenueByShopId(Long shopId, Instant dateFrom, Instant dateTo);
    Long revenuePerEmployee(Long userId, Instant dateFrom, Instant dateTo);
    Long revenuePerEmployeeByShopId(Long userId, Long shopId, Instant dateFrom, Instant dateTo);
    Long totalOrder(Instant dateFrom, Instant dateTo);
    Long totalOrderByShopId(Long shopId, Instant from, Instant to);
}
