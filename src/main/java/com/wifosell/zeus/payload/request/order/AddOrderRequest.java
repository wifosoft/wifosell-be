package com.wifosell.zeus.payload.request.order;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
public class AddOrderRequest implements IOrderRequest {
    @NotEmpty
    private List<OrderItem> orderItems;

    @NonNull
    private Long shopId;

    @NonNull
    private Long saleChannelId;

    @NonNull
    private Long customerId;

    private Boolean isActive;
}
