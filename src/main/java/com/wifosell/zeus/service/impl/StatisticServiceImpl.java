package com.wifosell.zeus.service.impl;


import com.wifosell.zeus.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;

@Transactional
@Service("Statistic")
@RequiredArgsConstructor
public class StatisticServiceImpl {
    private final OrderRepository orderRepository;

    Long totalOrder(Date dateFrom, Date dateTo){
        return this.orderRepository.getTotalOrder(dateFrom, dateTo);
    }

    Long totalOrderByShopId(Date dateFrom, Date dateTo, Long shopId){
        return this.orderRepository.getTotalOrderByShopId(shopId, dateFrom, dateTo);
    }
}
