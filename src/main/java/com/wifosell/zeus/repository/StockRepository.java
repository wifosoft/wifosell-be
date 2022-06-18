package com.wifosell.zeus.repository;

import com.wifosell.framework.repository.SoftRepository;
import com.wifosell.zeus.model.stock.Stock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockRepository extends SoftRepository<Stock, Long> {
    Stock getStockByWarehouseIdAndVariantId(Long warehouseId, Long variantId);

    @Query("select e from Stock e where e.variant.id = ?1 and e.warehouse.id = ?2")
    Optional<Stock> findByVariantAndWarehouse(Long variantId, Long warehouseId);
}
