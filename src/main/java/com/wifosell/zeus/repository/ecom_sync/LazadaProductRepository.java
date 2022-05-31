package com.wifosell.zeus.repository.ecom_sync;

import com.wifosell.zeus.model.attribute.Attribute;
import com.wifosell.zeus.model.ecom_sync.LazadaProduct;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LazadaProductRepository extends CrudRepository<LazadaProduct, Long> {
}
