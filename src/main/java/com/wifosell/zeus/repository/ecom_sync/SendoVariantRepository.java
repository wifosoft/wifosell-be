
package com.wifosell.zeus.repository.ecom_sync;

import com.wifosell.framework.repository.SoftRepository;
import com.wifosell.zeus.model.ecom_sync.EcomAccount;
import com.wifosell.zeus.model.ecom_sync.SendoVariant;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SendoVariantRepository extends SoftRepository<SendoVariant, Long> {
    boolean existsBySkuId(Long skuId);

    SendoVariant findBySkuId(String skuId);

    @Query("select u from SendoVariant u where u.skuId = :skuId and u.ecomAccount.id = :ecomId")
    SendoVariant findBySkuIdAndEcomAccountId(@Param("skuId") String skuId, @Param("ecomId") Long ecomId);


}
