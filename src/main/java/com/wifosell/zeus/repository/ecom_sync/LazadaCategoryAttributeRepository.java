package com.wifosell.zeus.repository.ecom_sync;

import com.wifosell.framework.repository.SoftRepository;
import com.wifosell.zeus.model.ecom_sync.LazadaCategory;
import com.wifosell.zeus.model.ecom_sync.LazadaCategoryAttribute;
import org.springframework.stereotype.Repository;

@Repository
public interface LazadaCategoryAttributeRepository extends SoftRepository<LazadaCategoryAttribute, Long> {
}
