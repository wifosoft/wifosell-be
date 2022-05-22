package com.wifosell.zeus.payload.response.order;

import com.wifosell.zeus.model.customer.Customer;
import com.wifosell.zeus.model.order.OrderItem;
import com.wifosell.zeus.model.order.OrderModel;
import com.wifosell.zeus.model.order.Payment;
import com.wifosell.zeus.model.sale_channel.SaleChannel;
import com.wifosell.zeus.model.shop.Shop;
import com.wifosell.zeus.payload.response.BasicEntityResponse;
import com.wifosell.zeus.payload.response.product.VariantResponse;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class OrderResponse extends BasicEntityResponse {
    private final List<OrderItemResponse> orderItems;
    private final ShopResponse shop;
    private final SaleChannelResponse saleChannel;
    private final CustomerResponse customer;
    private final BigDecimal subtotal;
    private final OrderModel.STATUS status;
    private final PaymentResponse payment;
    private final Long invoiceId;

    public OrderResponse(OrderModel order) {
        super(order);
        this.orderItems = order.getOrderItems().stream().map(OrderItemResponse::new).collect(Collectors.toList());
        this.shop = new ShopResponse(order.getShop());
        this.saleChannel = new SaleChannelResponse(order.getSaleChannel());
        this.customer = new CustomerResponse(order.getCustomer());
        this.subtotal = order.getSubtotal();
        this.status = order.getStatus();
        this.payment = new PaymentResponse(order.getPayment());
        this.invoiceId = order.getInvoice() == null ? null : order.getInvoice().getId();
    }

    @Getter
    private static class OrderItemResponse extends BasicEntityResponse {
        private final BigDecimal price;
        private final Integer quantity;
        private final VariantResponse variant;

        public OrderItemResponse(OrderItem orderItem) {
            super(orderItem);
            this.price = orderItem.getPrice();
            this.quantity = orderItem.getQuantity();
            this.variant = new VariantResponse(orderItem.getVariant());
        }
    }

    @Getter
    private static class ShopResponse extends BasicEntityResponse {
        private final String name;
        private final String address;
        private final String phone;

        public ShopResponse(Shop shop) {
            super(shop);
            this.name = shop.getName();
            this.address = shop.getAddress();
            this.phone = shop.getPhone();
        }
    }

    @Getter
    private static class SaleChannelResponse extends BasicEntityResponse {
        private final String name;

        public SaleChannelResponse(SaleChannel saleChannel) {
            super(saleChannel);
            this.name = saleChannel.getName();
        }
    }

    @Getter
    private static class CustomerResponse extends BasicEntityResponse {
        private final String fullName;
        private final String dob;
        private final Integer sex;
        private final String phone;
        private final String email;
        private final String cin;
        private final String nation;
        private final String city;
        private final String district;
        private final String ward;
        private final String addressDetail;

        public CustomerResponse(Customer customer) {
            super(customer);
            this.fullName = customer.getFullName();
            this.dob = customer.getDob().toString();
            this.sex = customer.getSex().ordinal();
            this.phone = customer.getPhone();
            this.email = customer.getEmail();
            this.cin = customer.getCin();
            this.nation = customer.getNation();
            this.city = customer.getCity();
            this.district = customer.getDistrict();
            this.ward = customer.getWard();
            this.addressDetail = customer.getAddressDetail();
        }
    }

    @Getter
    private static class PaymentResponse extends BasicEntityResponse {
        private final Payment.METHOD method;
        private final Payment.STATUS status;
        private final String info;

        public PaymentResponse(Payment payment) {
            super(payment);
            this.method = payment.getMethod();
            this.status = payment.getStatus();
            this.info = payment.getInfo();
        }
    }
}
