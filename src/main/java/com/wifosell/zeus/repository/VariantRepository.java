package com.wifosell.zeus.repository;

import com.wifosell.framework.repository.SoftRepository;
import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.model.product.Variant;
import org.springframework.stereotype.Repository;

@Repository
public interface VariantRepository extends SoftRepository<Variant, Long> {
    default EAppExceptionCode getExceptionCodeEntityNotFound() {
        return EAppExceptionCode.VARIANT_NOT_FOUND;
    }

    void deleteAllByProductId(Long productId);
}
