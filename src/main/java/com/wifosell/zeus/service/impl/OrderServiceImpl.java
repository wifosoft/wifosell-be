package com.wifosell.zeus.service.impl;

import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.exception.AppException;
import com.wifosell.zeus.model.customer.Customer;
import com.wifosell.zeus.model.order.OrderItem;
import com.wifosell.zeus.model.order.OrderModel;
import com.wifosell.zeus.model.product.Variant;
import com.wifosell.zeus.model.sale_channel.SaleChannel;
import com.wifosell.zeus.model.shop.Shop;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.payload.GApiErrorBody;
import com.wifosell.zeus.payload.request.order.AddOrderRequest;
import com.wifosell.zeus.payload.request.order.IOrderRequest;
import com.wifosell.zeus.payload.request.order.UpdateOrderRequest;
import com.wifosell.zeus.repository.*;
import com.wifosell.zeus.service.OrderService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("OrderService")
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final VariantRepository variantRepository;
    private final OrderItemRepository orderItemRepository;
    private final ShopRepository shopRepository;
    private final SaleChannelRepository saleChannelRepository;
    private final SaleChannelShopRelationRepository saleChannelShopRelationRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

    @Override
    public List<OrderModel> getAllOrders(
            Boolean isActive
    ) {
        if (isActive == null)
            return orderRepository.findAll();
        return orderRepository.findAllWithActive(isActive);
    }

    @Override
    public List<OrderModel> getOrders(
            @NonNull Long userId,
            Boolean isActive
    ) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        if (isActive == null)
            return orderRepository.findAllWithGm(gm.getId());
        return orderRepository.findAllWithGmAndActive(gm.getId(), isActive);
    }

    @Override
    public List<OrderModel> getOrdersByShopId(
            @NonNull Long userId,
            @NonNull Long shopId,
            Boolean isActive
    ) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        if (isActive == null)
            return orderRepository.findAllByShopIdWithGm(gm.getId(), shopId);
        return orderRepository.findAllByShopIdWithGmAndActive(gm.getId(), shopId, isActive);
    }

    @Override
    public List<OrderModel> getOrdersBySaleChannelId(
            @NonNull Long userId,
            @NonNull Long saleChannelId,
            Boolean isActive
    ) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        if (isActive == null)
            return orderRepository.findAllBySaleChannelIdWithGm(gm.getId(), saleChannelId);
        return orderRepository.findAllBySaleChannelIdWithGmAndActive(gm.getId(), saleChannelId, isActive);
    }

    @Override
    public List<OrderModel> getOrdersByShopIdAndSaleChannelId(
            @NonNull Long userId,
            @NonNull Long shopId,
            @NonNull Long saleChannelId,
            Boolean isActive
    ) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        if (isActive == null)
            return orderRepository.findAllByShopIdAndSaleChannelIdWithGm(gm.getId(), shopId, saleChannelId);
        return orderRepository.findAllByShopIdAndSaleChannelIdWithGmAndActive(gm.getId(), shopId, saleChannelId, isActive);
    }

    @Override
    public OrderModel getOrder(@NonNull Long userId, @NonNull Long orderId) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        return orderRepository.getByIdWithGm(gm.getId(), orderId);
    }

    @Override
    public OrderModel addOrder(@NonNull Long userId, AddOrderRequest request) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        OrderModel order = new OrderModel();
        return this.updateOrderByRequest(order, request, gm);
    }

    @Override
    public OrderModel updateOrder(@NonNull Long userId, @NonNull Long orderId, UpdateOrderRequest request) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        OrderModel order = orderRepository.getByIdWithGm(gm.getId(), orderId);
        return this.updateOrderByRequest(order, request, gm);
    }

    @Override
    public OrderModel activateOrder(@NonNull Long userId, @NonNull Long orderId) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        OrderModel order = orderRepository.getByIdWithGm(gm.getId(), orderId);
        order.setIsActive(true);
        return orderRepository.save(order);
    }

    @Override
    public OrderModel deactivateOrder(@NonNull Long userId, @NonNull Long orderId) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        OrderModel order = orderRepository.getByIdWithGm(gm.getId(), orderId);
        order.setIsActive(false);
        return orderRepository.save(order);
    }

    @Override
    public List<OrderModel> activateOrders(@NonNull Long userId, @NonNull List<Long> orderIds) {
        return orderIds.stream().map(id -> this.activateOrder(userId, id)).collect(Collectors.toList());
    }

    @Override
    public List<OrderModel> deactivateOrders(@NonNull Long userId, @NonNull List<Long> orderIds) {
        return orderIds.stream().map(id -> this.deactivateOrder(userId, id)).collect(Collectors.toList());
    }

    private OrderModel updateOrderByRequest(OrderModel order, IOrderRequest request, User gm) {
        // Order items
        Optional.ofNullable(request.getOrderItems()).ifPresent(orderItemRequests -> {
            orderItemRepository.deleteAllByOrderId(order.getId());
            order.getOrderItems().clear();

            List<OrderItem> orderItems = new ArrayList<>();
            for (IOrderRequest.OrderItem orderItemRequest : orderItemRequests) {
                Variant variant = variantRepository.getById(orderItemRequest.getVariantId());
                OrderItem orderItem = OrderItem.builder()
                        .variant(variant)
                        .price(variant.getCost())
                        .quantity(orderItemRequest.getQuantity())
                        .note(orderItemRequest.getNote())
                        .order(order)
                        .build();
                orderItems.add(orderItem);
            }

            order.getOrderItems().addAll(orderItems);
            orderItemRepository.saveAll(orderItems);
            orderRepository.save(order);
        });

        // Sale Channel & Shop
        Optional.ofNullable(request.getShopId()).ifPresent(shopId -> {
            Optional.ofNullable(request.getSaleChannelId()).ifPresent(saleChannelId -> {
                if (saleChannelShopRelationRepository.existsSaleChannelShopRelationByShopAndSaleChannel(shopId, saleChannelId)) {
                    Shop shop = shopRepository.getByIdWithGm(gm.getId(), shopId);
                    order.setShop(shop);

                    SaleChannel saleChannel = saleChannelRepository.getByIdWithGm(gm.getId(), saleChannelId);
                    order.setSaleChannel(saleChannel);
                } else {
                    throw new AppException(GApiErrorBody.makeErrorBody(EAppExceptionCode.SALE_CHANNEL_SHOP_RELATION_NOT_FOUND));
                }
            });
        });

        // Customer
        Optional.ofNullable(request.getCustomerId()).ifPresent(customerId -> {
            Customer customer = customerRepository.getByIdWithGm(gm.getId(), customerId);
            order.setCustomer(customer);
        });

        // Active
        Optional.ofNullable(request.getActive()).ifPresent(order::setIsActive);

        // General manager
        order.setGeneralManager(gm);

        // Subtotal
        BigDecimal subtotal = order.calcSubTotal();
        order.setSubtotal(subtotal);

        return orderRepository.save(order);
    }
}
