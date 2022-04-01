package com.wifosell.zeus.repository;

import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.exception.AppException;
import com.wifosell.zeus.model.shop.SaleChannelShopRelation;
import com.wifosell.zeus.payload.GApiErrorBody;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleChannelShopRelationRepository extends JpaRepository<SaleChannelShopRelation, Long> {
    @Query("select case when count(r.id) > 0 then true else false end from SaleChannelShopRelation r where  r.shop.id = ?1 and r.saleChannel.id = ?2")
    boolean existsSaleChannelShopRelationByShopAndSaleChannel(Long shopId, Long saleChannelId);

    SaleChannelShopRelation findSaleChannelShopRelationByShopIdAndSaleChannelId(Long shopId, Long saleChannelId);

    default SaleChannelShopRelation getSaleChannelShopRelationByShopIdAndSaleChannelId(Long shopId, Long saleChannelId) {
        SaleChannelShopRelation relation = this.findSaleChannelShopRelationByShopIdAndSaleChannelId(shopId, saleChannelId);
        if (relation == null) {
            throw new AppException(GApiErrorBody.makeErrorBody(EAppExceptionCode.SALE_CHANNEL_SHOP_RELATION_NOT_FOUND));
        }
        return relation;
    }
}
