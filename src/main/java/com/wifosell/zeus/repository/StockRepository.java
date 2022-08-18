package com.wifosell.zeus.repository;

import com.wifosell.framework.repository.SoftRepository;
import com.wifosell.zeus.model.stock.Stock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends SoftRepository<Stock, Long> {
    Stock getStockByWarehouseIdAndVariantId(Long warehouseId, Long variantId);


    //Lấy danh sách unique warehouse để kiếm ecom account liên kết, và sau đó cập nhật: sendo thông qua (productId, ecomId)
    @Query("select u from Stock u where u.variant.id = :variantId group by u.warehouse.id")
    List<Stock> findAllByVariantIdGroupByWarehouse(@Param("variantId") Long variantId);



    @Query("select e from Stock e where e.variant.id = ?1 and e.warehouse.id = ?2")
    Optional<Stock> findByVariantAndWarehouse(Long variantId, Long warehouseId);
}
