package com.wifosell.zeus.repository;

import com.wifosell.framework.repository.SoftRepository;
import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.model.product.Product;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends SoftRepository<Product, Long> {
    @Override
    default EAppExceptionCode getExceptionCodeEntityNotFound() {
        return EAppExceptionCode.PRODUCT_NOT_FOUND;
    }
}
