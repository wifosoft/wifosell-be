package com.wifosell.zeus.payload.response.stats;

import com.wifosell.zeus.model.stats.TopRevenueVariant;
import com.wifosell.zeus.payload.response.product.VariantResponse;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class TopRevenueVariantResponse {
    private final VariantResponse variant;
    private final BigDecimal revenue;

    public TopRevenueVariantResponse(TopRevenueVariant topRevenueVariant) {
        this.variant = new VariantResponse(topRevenueVariant.getVariant());
        this.revenue = topRevenueVariant.getRevenue();
    }
}
