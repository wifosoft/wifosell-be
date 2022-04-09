package com.wifosell.zeus.service;

import com.wifosell.zeus.model.order.OrderModel;
import com.wifosell.zeus.payload.request.order.AddOrderRequest;
import com.wifosell.zeus.payload.request.order.UpdateOrderRequest;
import lombok.NonNull;

import javax.validation.Valid;
import java.util.List;

public interface OrderService {
    List<OrderModel> getAllOrders(Boolean isActive);

    List<OrderModel> getOrders(@NonNull Long userId, Boolean isActive);

    List<OrderModel> getOrdersByShopIds(@NonNull Long userId, @NonNull List<Long> shopIds, Boolean isActive);

    List<OrderModel> getOrdersBySaleChannelIds(@NonNull Long userId, @NonNull List<Long> saleChannelIds, Boolean isActive);

    List<OrderModel> getOrdersByShopIdsAndSaleChannelIds(@NonNull Long userId, @NonNull List<Long> shopIds, @NonNull List<Long> saleChannelIds, Boolean isActive);

    OrderModel getOrder(@NonNull Long userId, @NonNull Long orderId);

    OrderModel addOrder(@NonNull Long userId, @Valid AddOrderRequest request);

    OrderModel updateOrder(@NonNull Long userId, @NonNull Long orderId, @Valid UpdateOrderRequest request);

    OrderModel activateOrder(@NonNull Long userId, @NonNull Long orderId);

    OrderModel deactivateOrder(@NonNull Long userId, @NonNull Long orderId);

    List<OrderModel> activateOrders(@NonNull Long userId, @NonNull List<Long> orderIds);

    List<OrderModel> deactivateOrders(@NonNull Long userId, @NonNull List<Long> orderIds);
}
