package com.wifosell.zeus.repository;

import com.wifosell.framework.repository.GMSoftRepository;
import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.model.product.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ProductRepository extends GMSoftRepository<Product, Long> {
    @Override
    default EAppExceptionCode getExceptionCodeEntityNotFound() {
        return EAppExceptionCode.PRODUCT_NOT_FOUND;
    }

    @Transactional
    @Query("select distinct p from Product p " +
            "join Variant v on p.id = v.product.id " +
            "join Stock s on v.id = s.variant.id " +
            "where s.warehouse.id = ?1")
    List<Product> getAllByWarehouseId(Long warehouseId);
}
