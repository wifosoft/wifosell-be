package com.wifosell.zeus.repository.ecom_sync;

import com.wifosell.framework.repository.SoftRepository;
import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.model.ecom_sync.LazadaProductAndSysProduct;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LazadaProductAndSysProductRepository extends SoftRepository<LazadaProductAndSysProduct, Long> {
    @Override
    default EAppExceptionCode getExceptionCodeEntityNotFound() {
        return EAppExceptionCode.LAZADA_PRODUCT_LINK_SYS_PRODUCT_NOT_FOUND;
    }

    Optional<LazadaProductAndSysProduct> findByLazadaProductId(Long lazadaProductId);

    List<LazadaProductAndSysProduct> findAllBySysProductId(Long sysProductId);

    @Query("select l from LazadaProductAndSysProduct l where l.sysProduct.id = ?1 and l.lazadaProduct.ecomAccount.id = ?2")
    Optional<LazadaProductAndSysProduct> findBySysProductId(Long sysProductId, Long ecomId);
}
