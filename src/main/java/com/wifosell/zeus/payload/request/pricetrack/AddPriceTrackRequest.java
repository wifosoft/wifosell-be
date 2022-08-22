package com.wifosell.zeus.payload.request.pricetrack;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
public class AddPriceTrackRequest {
    @NotNull
    private Long variantId;

    @NotNull
    private String competitorUrl;

    @NotNull
    private BigDecimal competitorPrice;

    @NotNull
    private Boolean isAutoChangePrice;

    private BigDecimal deltaPrice;

    private BigDecimal minPrice;

    private BigDecimal maxPrice;

    private Boolean isActive;
}
