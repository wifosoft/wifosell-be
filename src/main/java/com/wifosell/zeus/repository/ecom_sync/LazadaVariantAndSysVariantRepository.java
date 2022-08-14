package com.wifosell.zeus.repository.ecom_sync;

import com.wifosell.framework.repository.SoftRepository;
import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.model.ecom_sync.LazadaVariantAndSysVariant;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LazadaVariantAndSysVariantRepository extends SoftRepository<LazadaVariantAndSysVariant, Long> {
    @Override
    default EAppExceptionCode getExceptionCodeEntityNotFound() {
        return EAppExceptionCode.LAZADA_VARIANT_LINK_SYS_VARIANT_NOT_FOUND;
    }

    Optional<LazadaVariantAndSysVariant> findBySysVariantId(Long sysVariantId);

    Optional<LazadaVariantAndSysVariant> findByLazadaVariantId(Long lazadaVariantId);
}
