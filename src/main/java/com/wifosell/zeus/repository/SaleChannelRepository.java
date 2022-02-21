package com.wifosell.zeus.repository;

import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.exception.AppException;
import com.wifosell.zeus.model.sale_channel.SaleChannel;
import com.wifosell.zeus.payload.GApiErrorBody;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface SaleChannelRepository extends SoftDeleteCrudRepository<SaleChannel, Long> {
    default SaleChannel findSaleChannelById(Long saleChannelId) {
        return this.findSaleChannelById(saleChannelId, false);
    }

    default SaleChannel findSaleChannelById(Long saleChannelId, boolean includeInactive) {
        Optional<SaleChannel> optionalSaleChannel = includeInactive ? this.findById(saleChannelId) : this.findOne(saleChannelId);
        return optionalSaleChannel.orElseThrow(
                () -> new AppException(GApiErrorBody.makeErrorBody(EAppExceptionCode.SALE_CHANNEL_NOT_FOUND))
        );
    }

    @Transactional
    @Query("select sc from SaleChannel sc where sc.isActive = true and sc.generalManager.id = ?1")
    List<SaleChannel> findSaleChannelsByGeneralManagerId(Long generalManagerId);

    @Transactional
    @Query("select sc from SaleChannel sc join SaleChannelShopRelation r on sc.id = r.saleChannel.id where sc.isActive = true and r.shop.id = ?1")
    List<SaleChannel> findSaleChannelsByShopId(Long shopId);
}
