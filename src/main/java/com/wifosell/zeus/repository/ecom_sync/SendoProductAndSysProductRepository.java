package com.wifosell.zeus.repository.ecom_sync;

import com.wifosell.framework.repository.SoftRepository;
import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.model.ecom_sync.LazadaProductAndSysProduct;
import com.wifosell.zeus.model.ecom_sync.SendoProductAndSysProduct;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SendoProductAndSysProductRepository extends SoftRepository<SendoProductAndSysProduct, Long> {
    @Override
    default EAppExceptionCode getExceptionCodeEntityNotFound() {
        return EAppExceptionCode.SENDO_PRODUCT_LINK_SYS_PRODUCT_NOT_FOUND;
    }



    Optional<SendoProductAndSysProduct> findBySendoProductId(Long sendoProductId);

    @Query("select u from SendoProductAndSysProduct u where u.sysProduct.id = ?1")
    List<SendoProductAndSysProduct> findBySysProductId(Long sysProductId);
}
