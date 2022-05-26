package com.wifosell.zeus.payload.request.order;

import com.wifosell.zeus.model.order.OrderModel;
import com.wifosell.zeus.model.order.Payment;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateOrderRequest {
    private OrderModel.STATUS status;

    private PaymentRequest payment;

    private Boolean isActive;

    @Getter
    @Setter
    public static class PaymentRequest {
        private Payment.STATUS status;
    }
}
