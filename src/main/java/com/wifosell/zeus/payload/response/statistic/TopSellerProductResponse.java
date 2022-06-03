package com.wifosell.zeus.payload.response.statistic;

import com.wifosell.zeus.model.product.Variant;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TopSellerProductResponse {
    private Variant variant;
    private BigDecimal total;
    public TopSellerProductResponse (Variant variant, BigDecimal total) {
        this.variant = variant;
        this.total = total;
    }
}
