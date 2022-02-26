package com.wifosell.zeus.repository;

import com.wifosell.framework.repository.GMSoftRepository;
import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.model.sale_channel.SaleChannel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface SaleChannelRepository extends GMSoftRepository<SaleChannel, Long> {
    @Override
    default EAppExceptionCode getExceptionCodeEntityNotFound() {
        return EAppExceptionCode.SALE_CHANNEL_NOT_FOUND;
    }

    @Transactional
    @Query("select sc from SaleChannel sc join SaleChannelShopRelation r on sc.id = r.saleChannel.id where r.saleChannel.generalManager.id = ?1 and r.shop.generalManager.id = ?1 and r.shop.id = ?2")
    List<SaleChannel> findAllByShopIdWithGm(Long gmId, Long shopId);

    @Transactional
    @Query("select sc from SaleChannel sc join SaleChannelShopRelation r on sc.id = r.saleChannel.id where r.saleChannel.generalManager.id = ?1 and r.shop.generalManager.id = ?1 and r.shop.id = ?2 and sc.isActive = ?3")
    List<SaleChannel> findAllByShopIdWithGmAndActive(Long gmId, Long shopId, boolean isActive);
}
