package com.wifosell.zeus.repository.ecom_sync;

import com.wifosell.framework.repository.SoftRepository;
import com.wifosell.zeus.model.ecom_sync.LazadaCategoryAttribute;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LazadaCategoryAttributeRepository extends SoftRepository<LazadaCategoryAttribute, Long> {
    Optional<LazadaCategoryAttribute> findFirstByLazadaAttributeId(Long lazadaAttributeOptionId);

}
