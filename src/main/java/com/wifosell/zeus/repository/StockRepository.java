package com.wifosell.zeus.repository;

import com.wifosell.zeus.model.warehouse.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> findStockByWarehouseIdAndVariantId(Long warehouseId, Long variantId);
}
