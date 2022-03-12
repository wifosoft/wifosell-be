package com.wifosell.zeus.repository;

import com.wifosell.zeus.model.shop.SaleChannelShopRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleChannelShopRelationRepository extends JpaRepository<SaleChannelShopRelation, Long> {
    @Query("select case when count(r.id) > 0 then true else false end from SaleChannelShopRelation r where  r.shop.id = ?1 and r.saleChannel.id = ?2")
    boolean existsSaleChannelShopRelationByShopAndSaleChannel(Long shopId, Long saleChannelId);
}
