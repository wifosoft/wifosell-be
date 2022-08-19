package com.wifosell.zeus.repository;

import com.wifosell.zeus.model.shop.SaleChannelShop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface SaleChannelShopRepository extends JpaRepository<SaleChannelShop, Long> {
    @Transactional
    @Query("select case when count(r.id) > 0 then true else false end from SaleChannelShop r where  r.shop.id = ?1 and r.saleChannel.id = ?2")
    boolean existsSaleChannelShopRelationByShopAndSaleChannel(Long shopId, Long saleChannelId);

    @Query("select u from SaleChannelShop u where u.shop.id= ?1 and u.saleChannel.id =?2 and u.warehouse.id = ?3")
    Optional<SaleChannelShop> findRecordBySSWId(Long shopId, Long saleChannelId, Long warehouseId);

    @Query("select u from SaleChannelShop u where u.warehouse.id = ?1")
    List<SaleChannelShop> findListSSWByWarehouseId(Long warehouseId);

    @Transactional
    void deleteByShopIdAndSaleChannelId(Long shopId, Long saleChannelId);

    SaleChannelShop getByShopIdAndSaleChannelId(Long shopId, Long saleChannelId);
}
