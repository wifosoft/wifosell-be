package com.wifosell.zeus.repository.ecom_sync;

import com.wifosell.zeus.model.ecom_sync.LazadaProduct;
import com.wifosell.zeus.model.ecom_sync.LazadaVariant;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LazadaVariantRepository extends CrudRepository<LazadaVariant, Long> {
}
