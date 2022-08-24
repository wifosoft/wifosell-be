package com.wifosell.zeus.payload.response.pricetrack;

import com.wifosell.zeus.model.pricetrack.PriceTrack;
import com.wifosell.zeus.payload.response.BasicEntityResponse;
import com.wifosell.zeus.payload.response.product.VariantResponse;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Optional;

@Getter
public class PriceTrackResponse extends BasicEntityResponse {


    private VariantResponse variantResponse;

    private String competitorUrl;

    private BigDecimal competitorPrice;


    private boolean isAutoChangePrice;

    private BigDecimal deltaPrice;

    private BigDecimal minPrice;

    private BigDecimal maxPrice;

    public PriceTrackResponse(PriceTrack priceTrack) {
        super(priceTrack);
        this.competitorUrl = priceTrack.getCompetitorUrl();
        this.competitorPrice = priceTrack.getCompetitorPrice();
        this.deltaPrice = priceTrack.getDeltaPrice();
        this.minPrice = priceTrack.getMinPrice();
        this.maxPrice = priceTrack.getMaxPrice();
        this.isAutoChangePrice = priceTrack.getIsAutoChangePrice();

        Optional.ofNullable(priceTrack.getVariant()).ifPresent(e -> {
            this.variantResponse = new VariantResponse(priceTrack.getVariant());
        });
    }
}
