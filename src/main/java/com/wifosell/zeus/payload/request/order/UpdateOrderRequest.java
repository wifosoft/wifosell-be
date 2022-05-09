package com.wifosell.zeus.payload.request.order;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateOrderRequest implements IOrderRequest {
    private List<OrderItem> orderItems;

    private Long shopId;

    private Long saleChannelId;

    private Long customerId;

    private Boolean isActive;
}
