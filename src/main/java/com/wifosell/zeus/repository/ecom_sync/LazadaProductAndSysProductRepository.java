package com.wifosell.zeus.repository.ecom_sync;

import com.wifosell.framework.repository.SoftRepository;
import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.model.ecom_sync.LazadaProductAndSysProduct;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LazadaProductAndSysProductRepository extends SoftRepository<LazadaProductAndSysProduct, Long> {
    @Override
    default EAppExceptionCode getExceptionCodeEntityNotFound() {
        return EAppExceptionCode.LAZADA_PRODUCT_LINK_SYS_PRODUCT_NOT_FOUND;
    }

    Optional<LazadaProductAndSysProduct> findByLazadaProductId(Long lazadaProductId);
}
