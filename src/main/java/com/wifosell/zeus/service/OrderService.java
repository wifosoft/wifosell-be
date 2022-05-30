package com.wifosell.zeus.service;

import com.wifosell.zeus.model.order.OrderModel;
import com.wifosell.zeus.model.order.Payment;
import com.wifosell.zeus.payload.request.order.AddOrderRequest;
import com.wifosell.zeus.payload.request.order.UpdateOrderPaymentStatusRequest;
import com.wifosell.zeus.payload.request.order.UpdateOrderRequest;
import com.wifosell.zeus.payload.request.order.UpdateOrderStatusRequest;
import org.springframework.data.domain.Page;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

public interface OrderService {
    Page<OrderModel> getOrders(
            Long userId,
            List<Long> shopIds,
            List<Long> saleChannelIds,
            List<OrderModel.STATUS> statuses,
            List<Payment.METHOD> paymentMethods,
            List<Payment.STATUS> paymentStatuses,
            List<Boolean> isActives,
            Integer offset,
            Integer limit,
            String sortBy,
            String orderBy
    );

    OrderModel getOrder(Long userId, Long orderId);

    OrderModel addOrder(Long userId, @Valid AddOrderRequest request);

    OrderModel updateOrder(Long userId, Long orderId, UpdateOrderRequest request);

    OrderModel updateOrderStatus(Long userId, Long orderId, UpdateOrderStatusRequest request);

    OrderModel updateOrderPaymentStatus(Long userId, Long orderId, UpdateOrderPaymentStatusRequest request);

    OrderModel activateOrder(Long userId, Long orderId);

    OrderModel deactivateOrder(Long userId, Long orderId);

    List<OrderModel> activateOrders(Long userId, @NotNull List<Long> orderIds);

    List<OrderModel> deactivateOrders(Long userId, @NotNull List<Long> orderIds);
}
