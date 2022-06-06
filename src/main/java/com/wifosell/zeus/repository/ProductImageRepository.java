package com.wifosell.zeus.repository;

import com.wifosell.framework.repository.SoftRepository;
import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.model.product.ProductImage;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductImageRepository extends SoftRepository<ProductImage, Long> {
    default EAppExceptionCode getExceptionCodeEntityNotFound() {
        return EAppExceptionCode.PRODUCT_IMAGE_NOT_FOUND;
    }
}
