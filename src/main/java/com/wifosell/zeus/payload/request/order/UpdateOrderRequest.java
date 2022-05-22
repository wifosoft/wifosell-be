package com.wifosell.zeus.payload.request.order;

import com.wifosell.zeus.model.order.OrderModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateOrderRequest {
    private OrderModel.STATUS status;

    private Boolean isActive;
}
