package com.wifosell.zeus.payload.request.ecom_sync;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class EcomSyncUpdateStockRequest {
    @NotNull
    Long ecomId;

    @NotNull
    Long variantId;

    @NotNull
    Integer stock;
}
