package com.wifosell.zeus.repository.ecom_sync;

import com.wifosell.framework.repository.SoftRepository;
import com.wifosell.zeus.model.ecom_sync.LazadaVariant;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LazadaVariantRepository extends SoftRepository<LazadaVariant, Long> {
    boolean existsBySkuId(Long skuId);

    LazadaVariant getBySkuId(Long skuId);

    Optional<LazadaVariant> findBySkuId(Long skuId);
}
