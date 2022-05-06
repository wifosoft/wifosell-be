package com.wifosell.zeus.payload.request.stock;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Getter
@Setter
public class StockRequest {
    @NotNull
    Long variantId;

    @Positive
    Integer quantity;

    @NotBlank
    BigDecimal unitCost;
}
