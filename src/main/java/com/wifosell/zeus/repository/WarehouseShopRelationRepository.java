package com.wifosell.zeus.repository;

import com.wifosell.zeus.model.shop.WarehouseShopRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WarehouseShopRelationRepository extends JpaRepository<WarehouseShopRelation, Long> {
    @Query("SELECT CASE WHEN COUNT(u.id) > 0 THEN true ELSE false END FROM WarehouseShopRelation u WHERE  u.shop.id = (:shop_id) AND u.warehouse.id = (:warehouse_id)")
    boolean existsWarehouseShopRelationByShopAndWarehouse(@Param("shop_id") Long shopId,
                                                     @Param("warehouse_id") Long warehouseId);
}
