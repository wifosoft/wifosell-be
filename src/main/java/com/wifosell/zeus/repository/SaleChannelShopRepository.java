package com.wifosell.zeus.repository;

import com.wifosell.zeus.model.shop.SaleChannelShop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface SaleChannelShopRepository extends JpaRepository<SaleChannelShop, Long> {
    @Transactional
    @Query("select case when count(r.id) > 0 then true else false end from SaleChannelShop r where  r.shop.id = ?1 and r.saleChannel.id = ?2")
    boolean existsSaleChannelShopRelationByShopAndSaleChannel(Long shopId, Long saleChannelId);

    @Transactional
    void deleteByShopIdAndSaleChannelId(Long shopId, Long saleChannelId);
}
