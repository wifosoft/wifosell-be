package com.wifosell.zeus.database.seeder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.database.BaseSeeder;
import com.wifosell.zeus.database.ISeeder;
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
import com.wifosell.zeus.repository.*;
import com.wifosell.zeus.specs.CustomerSpecs;
import com.wifosell.zeus.specs.SaleChannelSpecs;
import com.wifosell.zeus.specs.ShopSpecs;
import com.wifosell.zeus.utils.FileUtils;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderSeeder extends BaseSeeder implements ISeeder {
    private OrderRepository orderRepository;
    private VariantRepository variantRepository;
    private OrderItemRepository orderItemRepository;
    private ShopRepository shopRepository;
    private SaleChannelRepository saleChannelRepository;
    private SaleChannelShopRepository saleChannelShopRepository;
    private CustomerRepository customerRepository;
    private UserRepository userRepository;
    private PaymentRepository paymentRepository;
    private OrderStepRepository orderStepRepository;

    @Override
    public void prepareJpaRepository() {
        this.orderRepository = this.factory.getRepository(OrderRepository.class);
        this.variantRepository = this.factory.getRepository(VariantRepository.class);
        this.orderItemRepository = this.factory.getRepository(OrderItemRepository.class);
        this.shopRepository = this.factory.getRepository(ShopRepository.class);
        this.saleChannelRepository = this.factory.getRepository(SaleChannelRepository.class);
        this.saleChannelShopRepository = this.factory.getRepository(SaleChannelShopRepository.class);
        this.customerRepository = this.factory.getRepository(CustomerRepository.class);
        this.userRepository = this.factory.getRepository(UserRepository.class);
        this.paymentRepository = this.factory.getRepository(PaymentRepository.class);
        this.orderStepRepository = this.factory.getRepository(OrderStepRepository.class);
    }

    @Override
    public void run() {
        User user = userRepository.getUserByName("manager1");

        try {
            InputStream file = (new FileUtils()).getFileAsIOStream("data/order.json");
            AddOrderRequest[] requests = new ObjectMapper().readValue(file, AddOrderRequest[].class);
            file.close();
            for (AddOrderRequest request : requests) {
                this.updateOrderByRequest(user, request);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateOrderByRequest(User user, AddOrderRequest request) {
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
        order.setStatus(OrderModel.STATUS.CREATED);

        // Steps
        OrderStep step = OrderStep.builder()
                .status(OrderModel.STATUS.CREATED)
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
        order.setComplete(false);

        // Cancel
        order.setCanceled(false);

        // Created by
        order.setCreatedBy(user);

        // General manager
        order.setGeneralManager(gm);

        // Active
        Optional.ofNullable(request.getIsActive()).ifPresent(order::setIsActive);

        orderRepository.save(order);
    }
}
