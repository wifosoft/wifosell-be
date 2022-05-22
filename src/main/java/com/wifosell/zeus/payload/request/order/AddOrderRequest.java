package com.wifosell.zeus.payload.request.order;

import com.wifosell.zeus.model.order.OrderModel;
import lombok.Getter;
import lombok.NonNull;
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

    @NonNull
    private Long shopId;

    @NonNull
    private Long saleChannelId;

    @NonNull
    private Long customerId;

    @NonNull
    private OrderModel.PAYMENT_METHOD paymentMethod;

    @NonNull
    private OrderModel.PAYMENT_STATUS paymentStatus;

    @NonNull
    private String paymentInfo;

    private Boolean isActive;

    @Getter
    @Setter
    public static class OrderItem {
        @NonNull
        private Long variantId;

        @Positive
        private Integer quantity;

        @Size(max = 1000)
        private String note;
    }
}
