package com.wifosell.zeus.payload.request.stock;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class ImportStocksFromExcelRequest {
    @NotNull
    Long warehouseId;

    @NonNull
    Long supplierId;

    @NotEmpty
    List<Item> items;

    @Getter
    @Setter
    public static class Item {
        @NotNull
        String variantSKU;

        @Positive
        Integer quantity;

        @NotBlank
        BigDecimal unitCost;
    }
}
