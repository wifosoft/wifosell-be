package com.wifosell.zeus.repository.ecom_sync;

import com.wifosell.framework.repository.SoftRepository;
import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.model.ecom_sync.LazadaProductAndSysProduct;
import com.wifosell.zeus.model.ecom_sync.SendoProductAndSysProduct;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SendoProductAndSysProductRepository extends SoftRepository<SendoProductAndSysProduct, Long> {
    @Override
    default EAppExceptionCode getExceptionCodeEntityNotFound() {
        return EAppExceptionCode.SENDO_PRODUCT_LINK_SYS_PRODUCT_NOT_FOUND;
    }

    Optional<SendoProductAndSysProduct> findBySendoProductId(Long sendoProductId);

    Optional<SendoProductAndSysProduct> findBySysProductId(Long sysProductId);
}
