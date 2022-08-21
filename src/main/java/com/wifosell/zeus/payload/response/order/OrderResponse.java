package com.wifosell.zeus.payload.response.order;

import com.wifosell.zeus.model.customer.Customer;
import com.wifosell.zeus.model.order.OrderItem;
import com.wifosell.zeus.model.order.OrderModel;
import com.wifosell.zeus.model.order.OrderStep;
import com.wifosell.zeus.model.order.Payment;
import com.wifosell.zeus.model.sale_channel.SaleChannel;
import com.wifosell.zeus.model.shop.SaleChannelShop;
import com.wifosell.zeus.model.shop.Shop;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.payload.response.BasicEntityResponse;
import com.wifosell.zeus.payload.response.product.VariantResponse;
import com.wifosell.zeus.payload.response.warehouse.WarehouseResponse;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
public class OrderResponse extends BasicEntityResponse {
    private List<OrderItemResponse> orderItems;
    private ShopResponse shop;
    private SaleChannelResponse saleChannel;
    private CustomerResponse customer;
    private BigDecimal subtotal;
    private BigDecimal shippingFee;
    private BigDecimal total;
    private OrderModel.STATUS status;
    private List<OrderStepResponse> steps;
    private PaymentResponse payment;
    private boolean isComplete;
    private UserResponse createdBy;
    private Long sswId;

    public OrderResponse(OrderModel order) {
        super(order);
        this.orderItems = order.getOrderItems().stream().map(OrderItemResponse::new).collect(Collectors.toList());
        this.shop = new ShopResponse(order.getShop());
        this.saleChannel = new SaleChannelResponse(order.getSaleChannel());
        this.customer = new CustomerResponse(order.getCustomer());
        this.subtotal = order.getSubtotal();
        this.shippingFee = order.getShippingFee();
        this.total = order.getTotal();
        this.status = order.getStatus();
        this.steps = order.getSteps().stream().map(OrderStepResponse::new).collect(Collectors.toList());
        this.payment = new PaymentResponse(order.getPayment());
        this.isComplete = order.getIsComplete();
        this.createdBy = order.getCreatedBy() != null ? new UserResponse(order.getCreatedBy()) : null;
        Optional.ofNullable(order.getSaleChannelShop()).ifPresent(e->{
            this.sswId =e.getId();
        });
    }

    public boolean getIsComplete() {
        return isComplete;
    }

    @Getter
    private static class OrderItemResponse extends BasicEntityResponse {
        private final BigDecimal originalPrice;
        private final BigDecimal price;
        private final Integer quantity;
        private final BigDecimal subtotal;
        private final VariantResponse variant;

        public OrderItemResponse(OrderItem orderItem) {
            super(orderItem);
            this.originalPrice = orderItem.getOriginalPrice();
            this.price = orderItem.getPrice();
            this.quantity = orderItem.getQuantity();
            this.subtotal = orderItem.getSubtotal();
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
    @Setter
    private static class CustomerResponse extends BasicEntityResponse {
        private String fullName;
        private String dob;
        private Integer sex;
        private String phone;
        private String email;
        private String cin;
        private String nation;
        private String city;
        private String district;
        private String ward;
        private String addressDetail;

        public CustomerResponse(Customer customer) {
            super(customer);
            this.fullName = customer.getFullName();
            Optional.ofNullable(customer.getDob()).ifPresent(e -> {
                this.dob = e.toString();
            });
            Optional.ofNullable(customer.getSex()).ifPresent(e -> {
                this.sex = customer.getSex().ordinal();
            });
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
    private static class OrderStepResponse extends BasicEntityResponse {
        private final OrderModel.STATUS status;
        private final String note;
        private final User updatedBy;

        public OrderStepResponse(OrderStep step) {
            super(step);
            this.status = step.getStatus();
            this.note = step.getNote();
            this.updatedBy = step.getUpdatedBy();
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

    @Getter
    private static class UserResponse extends BasicEntityResponse {
        private final String firstName;
        private final String lastName;
        private final String username;
        private final String avatar;

        public UserResponse(User user) {
            super(user);
            this.firstName = user.getFirstName();
            this.lastName = user.getLastName();
            this.username = user.getUsername();
            this.avatar = user.getAvatar();
        }
    }
}
