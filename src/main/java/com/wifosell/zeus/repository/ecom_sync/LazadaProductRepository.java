package com.wifosell.zeus.repository.ecom_sync;

import com.wifosell.framework.repository.SoftRepository;
import com.wifosell.zeus.model.ecom_sync.LazadaProduct;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LazadaProductRepository extends SoftRepository<LazadaProduct, Long> {
    boolean existsByItemId(Long skuId);

    LazadaProduct getByItemId(Long itemId);

    Optional<LazadaProduct> findByItemId(Long itemId);
}
