package com.wifosell.zeus.payload.request.pricetrack;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class UpdatePriceTrackRequest {
    private Long variantId;

    private String competitorUrl;

    private BigDecimal competitorPrice;

    private Boolean isAutoChangePrice;

    private BigDecimal deltaPrice;

    private BigDecimal minPrice;

    private BigDecimal maxPrice;

    private Boolean isActive;
}
