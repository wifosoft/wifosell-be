package com.wifosell.zeus.service;

import com.wifosell.zeus.model.pricetrack.PriceTrack;
import com.wifosell.zeus.payload.request.pricetrack.AddPriceTrackRequest;
import com.wifosell.zeus.payload.request.pricetrack.UpdatePriceTrackRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface PriceTrackService {
    Map<Long, String> getMapActiveCompetitorUrls();

    int trigger(Long priceTrackId, BigDecimal newCompetitorPrice);

    List<PriceTrack> getPriceTracks(Long userId, List<Boolean> isActives);

    PriceTrack getPriceTrack(Long userId, Long priceTrackId);

    PriceTrack addPriceTrack(Long userId, AddPriceTrackRequest request);

    PriceTrack updatePriceTrack(Long userId, Long priceTrackId, UpdatePriceTrackRequest request);

    void deletePriceTrack(Long userId, Long priceTrackId);

    List<PriceTrack> deactivatePriceTracks(Long userId, List<Long> priceTrackIds);

    List<PriceTrack> activatePriceTracks(Long userId, List<Long> priceTrackIds);
}
