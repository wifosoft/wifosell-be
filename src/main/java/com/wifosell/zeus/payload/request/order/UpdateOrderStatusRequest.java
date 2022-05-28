package com.wifosell.zeus.payload.request.order;

import com.wifosell.zeus.model.order.OrderModel;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UpdateOrderStatusRequest {
    @NotNull
    private OrderModel.STATUS status;

    @NotNull
    private String note;
}
