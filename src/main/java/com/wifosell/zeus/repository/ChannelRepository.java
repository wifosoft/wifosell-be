package com.wifosell.zeus.repository;

import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.exception.AppException;
import com.wifosell.zeus.model.channel.Channel;
import com.wifosell.zeus.payload.GApiErrorBody;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface ChannelRepository extends SoftDeleteCrudRepository<Channel, Long>{
    default Channel findChannelById(Long channelId) {
        return this.findChannelById(channelId, false);
    }

    default Channel findChannelById(Long channelId, boolean includeInactive) {
        Optional<Channel> optionalChannel = includeInactive ? this.findById(channelId) : this.findOne(channelId);
        return optionalChannel.orElseThrow(
                () -> new AppException(GApiErrorBody.makeErrorBody(EAppExceptionCode.SALE_CHANNEL_NOT_FOUND))
        );
    }

    @Transactional
    @Query("select sc from Channel sc where sc.isActive = true and sc.generalManager.id = ?1")
    List<Channel> findChannelsByGeneralManagerId(Long generalManagerId);

    @Transactional
    @Query("select sc from Channel sc join SaleChannelShopRelation scsr on sc.id = scsr.saleChannel.id where sc.isActive = true and scsr.shop.id = ?1")
    List<Channel> findChannelsByShopId(Long shopId);
}
