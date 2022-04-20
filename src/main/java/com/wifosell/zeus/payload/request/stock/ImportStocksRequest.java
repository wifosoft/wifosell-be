package com.wifosell.zeus.payload.request.stock;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class ImportStocksRequest {
    @NotEmpty
    List<StockRequest> stocks;

    SupplierRequest supplier;

    @Getter
    @Setter
    public static class StockRequest {
        @NotNull
        Long variantId;

        @Positive
        Integer quantity;

        @NotBlank
        BigDecimal unitCost;
    }

    @Getter
    @Setter
    public static class SupplierRequest {
        @NotBlank
        String name;

        @NotBlank
        String phone;

        @Email
        String email;

        String nation;

        String city;

        String district;

        String ward;

        String addressDetail;
    }
}
