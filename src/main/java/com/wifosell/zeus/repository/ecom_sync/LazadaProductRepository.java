package com.wifosell.zeus.repository.ecom_sync;

import com.wifosell.framework.repository.SoftRepository;
import com.wifosell.zeus.model.ecom_sync.LazadaProduct;
import org.springframework.stereotype.Repository;

@Repository
public interface LazadaProductRepository extends SoftRepository<LazadaProduct, Long> {
    boolean existsByItemId(Long skuId);

    LazadaProduct findByItemId(Long itemId);
}
