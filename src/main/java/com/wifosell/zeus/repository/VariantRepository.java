package com.wifosell.zeus.repository;

import com.wifosell.framework.repository.SoftRepository;
import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.model.product.Variant;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VariantRepository extends SoftRepository<Variant, Long> {
    default EAppExceptionCode getExceptionCodeEntityNotFound() {
        return EAppExceptionCode.VARIANT_NOT_FOUND;
    }

    void deleteAllByProductId(Long productId);



    @Query("select e from Variant e where e.sku in (:skus)")
    List<Variant> findListBySku(@Param("skus") List<String> skus);

    default Variant getBySKUNoThrow(String sku, Long gmId) {
        Optional<Variant> optional = this.findBySkuAndGeneralManagerId(sku, gmId);
        return optional.orElse(null);
    }

    Optional<Variant> findBySkuAndGeneralManagerId(String sku, Long gmId);
}
