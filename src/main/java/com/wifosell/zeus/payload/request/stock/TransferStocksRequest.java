package com.wifosell.zeus.payload.request.stock;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Getter
@Setter
public class TransferStocksRequest {
    @NonNull
    Long fromWarehouseId;

    @NonNull
    Long toWarehouseId;

    @NotEmpty
    List<Item> items;

    @Getter
    @Setter
    public static class Item {
        @NotNull
        Long variantId;

        @Positive
        Integer quantity;
    }
}
