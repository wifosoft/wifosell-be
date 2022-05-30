package com.wifosell.zeus.payload.request.order;

import com.wifosell.zeus.model.order.Payment;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UpdateOrderPaymentStatusRequest {
    @NotNull
    private Payment.STATUS status;
}
