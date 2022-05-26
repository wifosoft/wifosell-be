package com.wifosell.zeus.service.impl;

import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.exception.AppException;
import com.wifosell.zeus.model.customer.Customer;
import com.wifosell.zeus.model.order.OrderItem;
import com.wifosell.zeus.model.order.OrderModel;
import com.wifosell.zeus.model.order.Payment;
import com.wifosell.zeus.model.product.Variant;
import com.wifosell.zeus.model.sale_channel.SaleChannel;
import com.wifosell.zeus.model.shop.Shop;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.payload.GApiErrorBody;
import com.wifosell.zeus.payload.request.order.AddOrderRequest;
import com.wifosell.zeus.payload.request.order.UpdateOrderRequest;
import com.wifosell.zeus.repository.*;
import com.wifosell.zeus.service.OrderService;
import com.wifosell.zeus.specs.CustomerSpecs;
import com.wifosell.zeus.specs.OrderSpecs;
import com.wifosell.zeus.specs.SaleChannelSpecs;
import com.wifosell.zeus.utils.ZeusUtils;
import lombok.RequiredArgsConstructor;
import org.apache.poi.sl.draw.geom.GuideIf;
import org.springframework.data.domain.Page;
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
    private final PaymentRepository paymentRepository;

    @Override
    public Page<OrderModel> getOrders(
            Long userId,
            List<Long> shopIds,
            List<Long> saleChannelIds,
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
                        .and(OrderSpecs.inIsActives(isActives)),
                ZeusUtils.getDefaultPageable(offset, limit, sortBy, orderBy)
        );
    }

    @Override
    public OrderModel getOrder(Long userId, Long orderId) {
        Long gmId = userId == null ? null : userRepository.getUserById(userId).getGeneralManager().getId();
        return orderRepository.getOne(
                OrderSpecs.hasGeneralManager(gmId).
                        and(OrderSpecs.hasId(orderId))
        );
    }

    @Override
    public OrderModel addOrder(Long userId, AddOrderRequest request) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        OrderModel order = new OrderModel();
        return this.addOrderByRequest(order, request, gm);
    }

    @Override
    public OrderModel updateOrder(Long userId, Long orderId, UpdateOrderRequest request) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        OrderModel order = getOrder(userId, orderId);
        return this.updateOrderByRequest(order, request);
    }

    @Override
    public OrderModel activateOrder(Long userId, Long orderId) {
        OrderModel order = getOrder(userId, orderId);
        order.setIsActive(true);
        return orderRepository.save(order);
    }

    @Override
    public OrderModel deactivateOrder(Long userId, Long orderId) {
        OrderModel order = getOrder(userId, orderId);
        order.setIsActive(false);
        return orderRepository.save(order);
    }

    @Override
    public List<OrderModel> activateOrders(Long userId, List<Long> orderIds) {
        return orderIds.stream().map(id -> this.activateOrder(userId, id)).collect(Collectors.toList());
    }

    @Override
    public List<OrderModel> deactivateOrders(Long userId, List<Long> orderIds) {
        return orderIds.stream().map(id -> this.deactivateOrder(userId, id)).collect(Collectors.toList());
    }

    private OrderModel addOrderByRequest(OrderModel order, AddOrderRequest request, User gm) {
        // Order items
        Optional.ofNullable(request.getOrderItems()).ifPresent(orderItemRequests -> {
            orderItemRepository.deleteAllByOrderId(order.getId());
            order.getOrderItems().clear();

            List<OrderItem> orderItems = new ArrayList<>();
            for (AddOrderRequest.OrderItem orderItemRequest : orderItemRequests) {
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
        Optional.of(request.getShopId()).ifPresent(shopId -> {
            Optional.of(request.getSaleChannelId()).ifPresent(saleChannelId -> {
                if (saleChannelShopRelationRepository.existsSaleChannelShopRelationByShopAndSaleChannel(shopId, saleChannelId)) {
                    Shop shop = shopRepository.getByIdWithGm(gm.getId(), shopId);
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

        // Active
        Optional.ofNullable(request.getIsActive()).ifPresent(order::setIsActive);

        // General manager
        order.setGeneralManager(gm);

        // Subtotal
        BigDecimal subtotal = order.calcSubTotal();
        order.setSubtotal(subtotal);

        // Payment
        Payment payment = Payment.builder()
                .method(request.getPayment().getMethod())
                .status(request.getPayment().getStatus())
                .info(request.getPayment().getInfo())
                .build();
        order.setPayment(paymentRepository.save(payment));

        return orderRepository.save(order);
    }

    private OrderModel updateOrderByRequest(OrderModel order, UpdateOrderRequest request) {
        Optional.ofNullable(request.getStatus()).ifPresent(order::setStatus);
        Optional.ofNullable(request.getIsActive()).ifPresent(order::setIsActive);
        Optional.ofNullable(request.getPayment()).flatMap(payment -> Optional.ofNullable(payment.getStatus())).ifPresent(status -> {
            order.getPayment().setStatus(status);
            paymentRepository.save(order.getPayment());
        });
        return orderRepository.save(order);
    }
}
