package com.wifosell.zeus.service.impl;


import com.wifosell.zeus.model.product.Product;
import com.wifosell.zeus.repository.OrderRepository;
import com.wifosell.zeus.service.StatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Transactional
@Service("Statistic")
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {
    private final OrderRepository orderRepository;

    @Override
    public List<Product> topSeller(Date from, Date to) {
        return null;
    }

    @Override
    public List<Product> topSellerByShopId(Long shopId, Date from, Date to) {
        return null;
    }

    @Override
    public Long grossRevenue(Date from, Date to) {
        return null;
    }

    @Override
    public Long grossRevenueByShopId(Long shopId, Date from, Date to) {
        return null;
    }

    @Override
    public Long revenuePerEmployee(Long userId, Date from, Date to) {
        return null;
    }

    @Override
    public Long revenuePerEmployeeByShopId(Long userId, Long shopId, Date from, Date to) {
        return null;
    }

    public Long totalOrder(Instant dateFrom, Instant dateTo){
        return this.orderRepository.countAllByCreatedAtBetween(dateFrom, dateTo);
    }


    public Long totalOrderByShopId(Long shopId, Instant dateFrom, Instant dateTo){
        return this.orderRepository.countAllByShopIdAndCreatedAtBetween(shopId, dateFrom, dateTo);
    }
}
