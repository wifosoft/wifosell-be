package com.wifosell.zeus.repository.ecom_sync;

import com.wifosell.framework.repository.SoftRepository;
import com.wifosell.zeus.model.ecom_sync.LazadaProduct;
import com.wifosell.zeus.model.ecom_sync.SendoProduct;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LazadaProductRepository extends SoftRepository<LazadaProduct, Long> {
    boolean existsByItemId(Long skuId);

    LazadaProduct getByItemId(Long itemId);

    Optional<LazadaProduct> findByItemId(Long itemId);


    @Query("select u from LazadaProduct u where u.generalManager.id = :gmId")
    List<LazadaProduct> getListLazadaProductByGm(@Param("gmId") Long gmId);


}

