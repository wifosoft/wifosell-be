package com.wifosell.zeus.repository;

import com.wifosell.framework.repository.SoftRepository;
import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.model.pricetrack.PriceTrack;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceTrackRepository extends SoftRepository<PriceTrack, Long> {
    @Override
    default EAppExceptionCode getExceptionCodeEntityNotFound() {
        return EAppExceptionCode.PRICE_TRACK_NOT_FOUND;
    }
}
