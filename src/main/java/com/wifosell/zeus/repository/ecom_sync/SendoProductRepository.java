package com.wifosell.zeus.repository.ecom_sync;

import com.wifosell.framework.repository.SoftRepository;
import com.wifosell.zeus.model.ecom_sync.SendoProduct;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SendoProductRepository extends SoftRepository<SendoProduct, Long> {
    boolean existsByItemId(Long skuId);


    SendoProduct findFirstByItemId(Long itemId);


    SendoProduct findFirstByItemIdAndEcomAccountId(Long itemId, Long ecomAccountId);



    @Query("select u from SendoProduct  u where u.generalManager.id = :gmId")
    List<SendoProduct> getListSendoProductByGm(@Param("gmId") Long gmId);
}
