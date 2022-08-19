package com.wifosell.zeus.service.impl;

import com.google.gson.Gson;
import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.exception.AppException;
import com.wifosell.zeus.model.customer.Customer;
import com.wifosell.zeus.model.order.OrderItem;
import com.wifosell.zeus.model.order.OrderModel;
import com.wifosell.zeus.model.order.OrderStep;
import com.wifosell.zeus.model.order.Payment;
import com.wifosell.zeus.model.product.Variant;
import com.wifosell.zeus.model.sale_channel.SaleChannel;
import com.wifosell.zeus.model.shop.Shop;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.payload.GApiErrorBody;
import com.wifosell.zeus.payload.request.order.AddOrderRequest;
import com.wifosell.zeus.payload.request.order.UpdateOrderPaymentStatusRequest;
import com.wifosell.zeus.payload.request.order.UpdateOrderRequest;
import com.wifosell.zeus.payload.request.order.UpdateOrderStatusRequest;
import com.wifosell.zeus.repository.*;
import com.wifosell.zeus.service.OrderService;
import com.wifosell.zeus.specs.CustomerSpecs;
import com.wifosell.zeus.specs.OrderSpecs;
import com.wifosell.zeus.specs.SaleChannelSpecs;
import com.wifosell.zeus.specs.ShopSpecs;
import com.wifosell.zeus.utils.ZeusUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
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
    private final SaleChannelShopRepository saleChannelShopRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final OrderStepRepository orderStepRepository;

    @Override
    public Page<OrderModel> getOrders(
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
    ) {
        Long gmId = userId == null ? null : userRepository.getUserById(userId).getGeneralManager().getId();
        return orderRepository.findAll(
                OrderSpecs.hasGeneralManager(gmId)
                        .and(OrderSpecs.inShops(shopIds))
                        .and(OrderSpecs.inSaleChannels(saleChannelIds))
                        .and(OrderSpecs.inStatuses(statuses))
                        .and(OrderSpecs.inPaymentMethods(paymentMethods))
                        .and(OrderSpecs.inPaymentStatuses(paymentStatuses))
                        .and(OrderSpecs.inIsActives(isActives)),
                ZeusUtils.getDefaultPageable(offset, limit, sortBy, orderBy)
        );
    }

    @Override
    public OrderModel getOrder(Long userId, @NotNull Long orderId) {
        Long gmId = userId == null ? null : userRepository.getUserById(userId).getGeneralManager().getId();
        return orderRepository.getOne(
                OrderSpecs.hasGeneralManager(gmId).
                        and(OrderSpecs.hasId(orderId))
        );
    }

    @Override
    public OrderModel addOrder(Long userId, AddOrderRequest request) {
        User user = userRepository.getUserById(userId);
        return this.addOrderByRequest(user, request);
    }

    @Override
    public OrderModel updateOrder(Long userId, @NotNull Long orderId, UpdateOrderRequest request) {
        OrderModel order = this.getOrder(userId, orderId);
        return this.updateOrderByRequest(order, request);
    }

    @Override
    public OrderModel updateOrderStatus(Long userId, @NotNull Long orderId, UpdateOrderStatusRequest request) {
        User user = userRepository.getUserById(userId);
        OrderModel order = this.getOrder(userId, orderId);
        return this.updateOrderStatusByRequest(user, order, request);
    }

    @Override
    public OrderModel updateOrderPaymentStatus(Long userId, Long orderId, UpdateOrderPaymentStatusRequest request) {
        OrderModel order = this.getOrder(userId, orderId);
        return this.updateOrderPaymentStatus(order, request);
    }

    @Override
    public OrderModel activateOrder(Long userId, @NotNull Long orderId) {
        OrderModel order = this.getOrder(userId, orderId);
        order.setIsActive(true);
        return orderRepository.save(order);
    }

    @Override
    public OrderModel deactivateOrder(Long userId, @NotNull Long orderId) {
        OrderModel order = this.getOrder(userId, orderId);
        order.setIsActive(false);
        return orderRepository.save(order);
    }

    @Override
    public List<OrderModel> activateOrders(Long userId, @NotNull List<Long> orderIds) {
        return orderIds.stream().map(id -> this.activateOrder(userId, id)).collect(Collectors.toList());
    }

    @Override
    public List<OrderModel> deactivateOrders(Long userId, @NotNull List<Long> orderIds) {
        return orderIds.stream().map(id -> this.deactivateOrder(userId, id)).collect(Collectors.toList());
    }

    private OrderModel addOrderByRequest(User user, AddOrderRequest request) {
        User gm = user.getGeneralManager();
        OrderModel order = OrderModel.builder().build();

        // Order items
        Optional.ofNullable(request.getOrderItems()).ifPresent(orderItemRequests -> {
            orderItemRepository.deleteAllByOrderId(order.getId());
            order.getOrderItems().clear();

            List<OrderItem> orderItems = new ArrayList<>();
            for (AddOrderRequest.OrderItem orderItemRequest : orderItemRequests) {
                Variant variant = variantRepository.getById(orderItemRequest.getVariantId());
                OrderItem orderItem = OrderItem.builder()
                        .variant(variant)
                        .originalPrice(variant.getOriginalCost())
                        .price(variant.getCost())
                        .quantity(orderItemRequest.getQuantity())
                        .subtotal(variant.getCost().multiply(BigDecimal.valueOf(orderItemRequest.getQuantity())))
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
        Optional.of(request.getShopId()).ifPresent(shopId -> {
            Optional.of(request.getSaleChannelId()).ifPresent(saleChannelId -> {
                if (saleChannelShopRepository.existsSaleChannelShopRelationByShopAndSaleChannel(shopId, saleChannelId)) {
                    Shop shop = shopRepository.getOne(
                            ShopSpecs.hasGeneralManager(gm.getId())
                                    .and(ShopSpecs.hasId(shopId))
                    );
                    order.setShop(shop);

                    SaleChannel saleChannel = saleChannelRepository.getOne(
                            SaleChannelSpecs.hasGeneralManager(gm.getId())
                                    .and(SaleChannelSpecs.hasId(saleChannelId))
                    );
                    order.setSaleChannel(saleChannel);
                } else {
                    throw new AppException(GApiErrorBody.makeErrorBody(EAppExceptionCode.SALE_CHANNEL_SHOP_RELATION_NOT_FOUND));
                }
            });
        });

        // Customer
        Optional.of(request.getCustomerId()).ifPresent(customerId -> {
            Customer customer = customerRepository.getOne(
                    CustomerSpecs.hasGeneralManager(gm.getId())
                            .and(CustomerSpecs.hasId(customerId))
            );
            order.setCustomer(customer);
        });

        // Subtotal
        BigDecimal subtotal = BigDecimal.ZERO;
        for (OrderItem orderItem : order.getOrderItems()) {
            subtotal = subtotal.add(orderItem.getSubtotal());
        }
        order.setSubtotal(subtotal);

        // Shipping fee
        Optional.of(request.getShippingFee()).ifPresent(order::setShippingFee);

        // Total
        order.setTotal(order.getSubtotal().add(order.getShippingFee()));

        // Cur step
        order.setStatus(request.getStatus() != null ? request.getStatus() : OrderModel.STATUS.CREATED);

        // Steps
        OrderStep step = OrderStep.builder()
                .status(order.getStatus())
                .note("")
                .order(order)
                .updatedBy(user)
                .build();
        List<OrderStep> steps = new ArrayList<>(List.of(step));
        order.setSteps(orderStepRepository.saveAll(steps));

        // Payment
        Payment payment = Payment.builder()
                .method(request.getPayment().getMethod())
                .status(request.getPayment().getStatus())
                .info(request.getPayment().getInfo())
                .build();
        order.setPayment(paymentRepository.save(payment));

        // Complete
        boolean isComplete = order.getStatus().equals(OrderModel.STATUS.COMPLETE) && order.getPayment().getStatus().equals(Payment.STATUS.PAID);
        order.setComplete(isComplete);

        // Cancel
        boolean isCanceled = order.getStatus().equals(OrderModel.STATUS.CANCELED);
        order.setCanceled(isCanceled);

        // Created by
        order.setCreatedBy(user);

        // General manager
        order.setGeneralManager(gm);

        Optional.ofNullable(request.getShippingDetail()).ifPresent(o -> {
            String encodeShippingDetail = (new Gson()).toJson(o);
            order.setShipDetail(encodeShippingDetail);
        });

        order.setOrderSource(request.getOrderSource());

        // Active
        Optional.ofNullable(request.getIsActive()).ifPresent(order::setIsActive);

        return orderRepository.save(order);
    }

    private OrderModel updateOrderByRequest(OrderModel order, UpdateOrderRequest request) {
        Optional.ofNullable(request.getIsActive()).ifPresent(order::setIsActive);
        return orderRepository.save(order);
    }

    private OrderModel updateOrderStatusByRequest(User user, OrderModel order, UpdateOrderStatusRequest request) {
        if (request.getStatus() != order.getStatus()) {
            order.setStatus(request.getStatus());
            OrderStep step = OrderStep.builder()
                    .status(order.getStatus())
                    .note(request.getNote())
                    .order(order)
                    .updatedBy(user)
                    .build();
            order.getSteps().add(orderStepRepository.save(step));

            boolean isComplete = order.getStatus().equals(OrderModel.STATUS.COMPLETE) && order.getPayment().getStatus().equals(Payment.STATUS.PAID);
            order.setComplete(isComplete);

            boolean isCanceled = order.getStatus().equals(OrderModel.STATUS.CANCELED);
            order.setCanceled(isCanceled);

            orderRepository.save(order);
        }
        return order;
    }

    private OrderModel updateOrderPaymentStatus(OrderModel order, UpdateOrderPaymentStatusRequest request) {
        if (request.getStatus() != order.getPayment().getStatus()) {
            order.getPayment().setStatus(request.getStatus());
            paymentRepository.save(order.getPayment());

            boolean isComplete = order.getStatus().equals(OrderModel.STATUS.COMPLETE) && order.getPayment().getStatus().equals(Payment.STATUS.PAID);
            order.setComplete(isComplete);
            orderRepository.save(order);
        }
        return order;
    }
}
