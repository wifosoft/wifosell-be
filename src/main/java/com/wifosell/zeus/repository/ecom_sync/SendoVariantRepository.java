
package com.wifosell.zeus.repository.ecom_sync;

import com.wifosell.framework.repository.SoftRepository;
import com.wifosell.zeus.model.ecom_sync.LazadaVariant;
import com.wifosell.zeus.model.ecom_sync.SendoVariant;
import org.springframework.stereotype.Repository;

@Repository
public interface SendoVariantRepository extends SoftRepository<SendoVariant, Long> {
    boolean existsBySkuId(Long skuId);

    SendoVariant findBySkuId(Long skuId);
}
