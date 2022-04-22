package com.wifosell.zeus.payload.request.order;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;

public interface IOrderRequest {
    List<OrderItem> getOrderItems();

    Long getShopId();

    Long getSaleChannelId();

    Long getCustomerId();

    Boolean getActive();

    @Getter
    @Setter
    class OrderItem {
        @NonNull
        private Long variantId;

        @Positive
        private Integer quantity;

        @Size(max = 1000)
        private String note;
    }
}
