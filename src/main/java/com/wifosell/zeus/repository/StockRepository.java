package com.wifosell.zeus.repository;

import com.wifosell.zeus.model.stock.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    Stock getStockByWarehouseIdAndVariantId(Long warehouseId, Long variantId);
}
