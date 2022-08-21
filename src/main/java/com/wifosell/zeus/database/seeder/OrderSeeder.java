package com.wifosell.zeus.database.seeder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wifosell.zeus.database.BaseSeeder;
import com.wifosell.zeus.database.ISeeder;
import com.wifosell.zeus.database.SeederConst;
import com.wifosell.zeus.model.order.OrderModel;
import com.wifosell.zeus.payload.request.order.AddOrderRequest;
import com.wifosell.zeus.repository.OrderRepository;
import com.wifosell.zeus.service.OrderService;
import com.wifosell.zeus.utils.FileUtils;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;

public class OrderSeeder extends BaseSeeder implements ISeeder {
    private OrderService orderService;
    private OrderRepository orderRepository;

    @Override
    public void prepareJpaRepository() {
        orderService = context.getBean(OrderService.class);
        orderRepository = context.getBean(OrderRepository.class);
    }

    @Override
    public void run() {
        try {
            InputStream file = (new FileUtils()).getFileAsIOStream("data/order.json");
            AddOrderRequest[] requests = new ObjectMapper().readValue(file, AddOrderRequest[].class);
            file.close();
            for (int i = 0; i < requests.length; ++i) {
                OrderModel order = orderService.addOrderNoCaculateStock(SeederConst.USER_ID, requests[i]);
                order.setCreatedAt(Instant.now().minusMillis(86400000L * (requests.length - 1 - i)));
                orderRepository.save(order);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
