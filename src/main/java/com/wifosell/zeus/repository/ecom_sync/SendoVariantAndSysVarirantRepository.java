package com.wifosell.zeus.repository.ecom_sync;

import com.wifosell.framework.repository.SoftRepository;
import com.wifosell.zeus.model.ecom_sync.SendoVariantAndSysVariant;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SendoVariantAndSysVarirantRepository extends SoftRepository<SendoVariantAndSysVariant, Long> {

    @Query("select e from SendoVariantAndSysVariant e where e.sendoVariant.id= ?1 and e.variant.id = ?2")
    SendoVariantAndSysVariant findFirstBySendoVariantSysVariant(Long sendoVariantId, Long sysVariantId);
}
