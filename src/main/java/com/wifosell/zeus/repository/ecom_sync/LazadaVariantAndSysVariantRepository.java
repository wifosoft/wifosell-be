package com.wifosell.zeus.repository.ecom_sync;

import com.wifosell.framework.repository.SoftRepository;
import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.model.ecom_sync.LazadaVariantAndSysVariant;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LazadaVariantAndSysVariantRepository extends SoftRepository<LazadaVariantAndSysVariant, Long> {
    @Override
    default EAppExceptionCode getExceptionCodeEntityNotFound() {
        return EAppExceptionCode.LAZADA_VARIANT_LINK_SYS_VARIANT_NOT_FOUND;
    }

    @Query("select l from LazadaVariantAndSysVariant l where l.sysVariant.id = ?1 and l.lazadaVariant.ecomAccount.id = ?2")
    Optional<LazadaVariantAndSysVariant> findBySysVariantId(Long sysVariantId, Long ecomId);

    Optional<LazadaVariantAndSysVariant> findByLazadaVariantId(Long lazadaVariantId);
}
