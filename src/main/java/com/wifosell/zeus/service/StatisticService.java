package com.wifosell.zeus.service;

import com.wifosell.zeus.model.product.Product;

import java.time.Instant;
import java.util.Date;
import java.util.List;

public interface StatisticService {

    List<Product> topSeller(Instant dateFrom, Instant dateTo);

    List<Product> topSellerByShopId(Long shopId, Date from, Date to);

    Long grossRevenue(Instant dateFrom, Instant dateTo);

    Long grossRevenueByShopId(Long shopId, Date from, Date to);

    Long revenuePerEmployee(Long userId, Date from, Date to);

    Long revenuePerEmployeeByShopId(Long userId, Long shopId, Date from, Date to);

    Long totalOrder(Instant dateFrom, Instant dateTo);

    Long totalOrderByShopId(Long shopId, Instant from, Instant to);
}
