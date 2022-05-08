package com.wifosell.zeus.repository;

import com.wifosell.framework.repository.GMSoftRepository;
import com.wifosell.framework.repository.SoftRepository;
import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.model.product.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ProductRepository extends SoftRepository<Product, Long> {
    @Override
    default EAppExceptionCode getExceptionCodeEntityNotFound() {
        return EAppExceptionCode.PRODUCT_NOT_FOUND;
    }
}
