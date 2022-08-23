package com.wifosell.zeus.repository;

import com.wifosell.framework.repository.SoftRepository;
import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.model.stock.Stock;
import com.wifosell.zeus.model.warehouse.Warehouse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@ApiIgnore
@Repository
public interface WarehouseRepository extends SoftRepository<Warehouse, Long> {
    @Override
    default EAppExceptionCode getExceptionCodeEntityNotFound() {
        return EAppExceptionCode.WAREHOUSE_NOT_FOUND;
    }

    @Query(value = "SELECT * FROM stock s WHERE s.variant_id IN :variant_ids GROUP BY s.warehouse_id", nativeQuery = true)
    List<Stock> findWarehousesByVariantId(@Param("variant_ids") List<Long> listVariants);
}
