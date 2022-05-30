package com.wifosell.zeus.service.impl;


import com.wifosell.zeus.repository.OrderRepository;
import com.wifosell.zeus.service.StatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@Service("StatisticService")
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {
    private final OrderRepository orderRepository;
}
