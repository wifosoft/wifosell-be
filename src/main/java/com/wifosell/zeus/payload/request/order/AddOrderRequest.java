package com.wifosell.zeus.payload.request.order;

import com.sun.istack.NotNull;
import com.wifosell.zeus.model.order.Payment;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
public class AddOrderRequest {
    @NotEmpty
    private List<OrderItem> orderItems;

    @NotNull
    private Long shopId;

    @NotNull
    private Long saleChannelId;

    @NotNull
    private Long customerId;

    @NotNull
    private PaymentRequest payment;

    private Boolean isActive;

    @Getter
    @Setter
    public static class OrderItem {
        @NotNull
        private Long variantId;

        @Positive
        private Integer quantity;

        @Size(max = 1000)
        private String note;
    }

    @Getter
    @Setter
    public static class PaymentRequest {
        @NotNull
        private Payment.METHOD method;

        @NotNull
        private Payment.STATUS status;

        @NotNull
        private String info;
    }
}
