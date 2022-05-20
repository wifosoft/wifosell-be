package com.wifosell.zeus.repository;

import com.wifosell.framework.repository.SoftRepository;
import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.exception.AppException;
import com.wifosell.zeus.model.product.Variant;
import com.wifosell.zeus.payload.GApiErrorBody;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VariantRepository extends SoftRepository<Variant, Long> {
    default EAppExceptionCode getExceptionCodeEntityNotFound() {
        return EAppExceptionCode.VARIANT_NOT_FOUND;
    }

    void deleteAllByProductId(Long productId);

    Optional<Variant> findBySku(String sku);

    default Variant getBySKU(String sku) {
        Optional<Variant> optional = this.findBySku(sku);
        return optional.orElseThrow(
                () -> new AppException(GApiErrorBody.makeErrorBody(this.getExceptionCodeEntityNotFound()))
        );
    }

    default Variant getBySKUNoThrow(String sku) {
        Optional<Variant> optional = this.findBySku(sku);
        return optional.orElse(null);
    }
}
