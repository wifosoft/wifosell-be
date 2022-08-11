package com.wifosell.zeus.repository.ecom_sync;

import com.wifosell.framework.repository.SoftRepository;
import com.wifosell.zeus.model.ecom_sync.LazadaProduct;
import com.wifosell.zeus.model.ecom_sync.SendoProduct;
import org.springframework.stereotype.Repository;

@Repository
public interface SendoProductRepository extends SoftRepository<SendoProduct, Long> {
    boolean existsByItemId(Long skuId);

    SendoProduct findByItemId(Long itemId);
}
