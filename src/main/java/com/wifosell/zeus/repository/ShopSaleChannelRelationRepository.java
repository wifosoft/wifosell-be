package com.wifosell.zeus.repository;

import com.wifosell.zeus.model.shop.ShopSaleChannelRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ShopSaleChannelRelationRepository extends JpaRepository<ShopSaleChannelRelation, Long> {
    @Query("select case when count(s.id) > 0 then true else false end from ShopSaleChannelRelation s where  s.shop.id = ?1 and s.saleChannel.id = ?2")
    boolean existsSaleChannelShopRelationByShopAndSaleChannel(Long shopId, Long saleChannelId);
}
