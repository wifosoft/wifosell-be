package com.wifosell.zeus.service;

import com.wifosell.zeus.model.order.OrderModel;
import com.wifosell.zeus.payload.request.order.AddOrderRequest;
import com.wifosell.zeus.payload.request.order.UpdateOrderRequest;
import lombok.NonNull;
import org.springframework.data.domain.Page;

import javax.validation.Valid;
import java.util.List;

public interface OrderService {
    Page<OrderModel> getOrders(Long userId, List<Long> shopIds, List<Long> saleChannelIds, List<Boolean> isActives,
                               Integer offset, Integer limit, String sortBy, String orderBy);

    OrderModel getOrder(Long userId, Long orderId);

    OrderModel addOrder(Long userId, @Valid AddOrderRequest request);

    OrderModel updateOrder(Long userId, Long orderId, @Valid UpdateOrderRequest request);

    OrderModel activateOrder(Long userId, Long orderId);

    OrderModel deactivateOrder(Long userId, Long orderId);

    List<OrderModel> activateOrders(Long userId, List<Long> orderIds);

    List<OrderModel> deactivateOrders(Long userId, List<Long> orderIds);
}
