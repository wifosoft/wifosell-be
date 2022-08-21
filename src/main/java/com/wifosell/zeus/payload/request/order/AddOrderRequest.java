package com.wifosell.zeus.payload.request.order;

import com.sun.istack.NotNull;
import com.wifosell.zeus.model.order.OrderModel;
import com.wifosell.zeus.model.order.Payment;
import com.wifosell.zeus.model.shop.SaleChannelShop;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
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


    //shopId, saleChannelId, warehouseId
    private Long sswId;


    @NotNull
    private Long customerId;



    private OrderModel.STATUS status;

    @NotNull
    private PaymentRequest payment;

    @NotNull
    private BigDecimal shippingFee;

    private Boolean isActive;

    private ShippingDetail shippingDetail;

    private int orderSource;

    @Getter
    @Setter
    public static class ShippingDetail{
        private String contactName;
        private String contactEmail;
        private String contactPhone;
        private String contactAddress;
    }

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
