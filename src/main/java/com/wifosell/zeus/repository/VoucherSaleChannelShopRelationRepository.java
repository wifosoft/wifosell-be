package com.wifosell.zeus.repository;

import com.wifosell.zeus.model.shop.VoucherSaleChannelShopRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VoucherSaleChannelShopRelationRepository extends JpaRepository<VoucherSaleChannelShopRelation, Long> {
    @Query("select case when count(r.id) > 0 then true else false end from VoucherSaleChannelShopRelation r where r.saleChannel.id = ?1 and r.shop.id = ?2")
    boolean existsSaleChannelShopRelation(Long saleChannelId, Long shopId);

    @Query("select case when count(r.id) > 0 then true else false end from VoucherSaleChannelShopRelation r where r.voucher.id = ?1 and r.saleChannel.id = ?2 and r.shop.id = ?3")
    boolean existsVoucherSaleChannelShopRelation(Long productId, Long saleChannelId, Long shopId);
}
