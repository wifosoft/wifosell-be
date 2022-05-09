package com.wifosell.zeus.service;

import com.wifosell.zeus.model.sale_channel.SaleChannel;
import com.wifosell.zeus.payload.request.sale_channel.SaleChannelRequest;
import lombok.NonNull;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SaleChannelService {
    Page<SaleChannel> getSaleChannels(Long userId, List<Long> shopIds, List<Boolean> isActives,
                                      Integer offset, Integer limit, String sortBy, String orderBy);

    SaleChannel getSaleChannel(Long userId, @NonNull Long saleChannelId);

    SaleChannel addSaleChannel(@NonNull Long userId, @NonNull SaleChannelRequest saleChannelRequest);

    SaleChannel updateSaleChannel(@NonNull Long userId, @NonNull Long saleChannelId, @NonNull SaleChannelRequest saleChannelRequest);

    SaleChannel activateSaleChannel(Long userId, @NonNull Long saleChannelId);

    SaleChannel deactivateSaleChannel(Long userId, @NonNull Long saleChannelId);

    List<SaleChannel> activateSaleChannels(Long userId, @NonNull List<Long> saleChannelIds);

    List<SaleChannel> deactivateSaleChannels(Long userId, @NonNull List<Long> saleChannelIds);
}
