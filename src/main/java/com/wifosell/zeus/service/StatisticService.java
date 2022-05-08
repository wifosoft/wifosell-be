package com.wifosell.zeus.service;

import com.wifosell.zeus.model.product.Product;

import java.util.Date;
import java.util.List;

public interface StatisticService {
    List<Product> topSeller(Date from, Date to);
    List<Product> topSellerByShopId(Long shopId, Date from, Date to);
    Long totalOrder(Date dateFrom, Date dateTo);
    Long totalOrderByShopId(Long shopId, Date from, Date to);
    Long grossRevenue(Date from, Date to);
    Long grossRevenueByShopId(Long shopId, Date from, Date to);
    Long revenuePerEmployee(Long userId, Date from, Date to);
    Long revenuePerEmployeeByShopId(Long userId, Long shopId, Date from, Date to);
}
