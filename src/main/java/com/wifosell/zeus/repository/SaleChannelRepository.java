package com.wifosell.zeus.repository;

import com.wifosell.framework.repository.SoftRepository;
import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.model.sale_channel.SaleChannel;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleChannelRepository extends SoftRepository<SaleChannel, Long> {
    @Override
    default EAppExceptionCode getExceptionCodeEntityNotFound() {
        return EAppExceptionCode.SALE_CHANNEL_NOT_FOUND;
    }
}
