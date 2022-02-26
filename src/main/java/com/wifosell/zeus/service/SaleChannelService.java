package com.wifosell.zeus.service;

import com.wifosell.zeus.model.sale_channel.SaleChannel;
import com.wifosell.zeus.payload.request.sale_channel.SaleChannelRequest;
import lombok.NonNull;

import java.util.List;

public interface SaleChannelService {
    List<SaleChannel> getAllSaleChannels(Boolean isActive);
    List<SaleChannel> getSaleChannelsByUserId(@NonNull Long userId, Boolean isActive);
    List<SaleChannel> getSaleChannelsByShopId(@NonNull Long userId, @NonNull Long shopId, Boolean isActive);
    SaleChannel getSaleChannel(@NonNull Long userId, @NonNull Long saleChannelId);
    SaleChannel addSaleChannel(@NonNull Long userId, @NonNull SaleChannelRequest saleChannelRequest);
    SaleChannel updateSaleChannel(@NonNull Long userId, @NonNull Long saleChannelId, @NonNull SaleChannelRequest saleChannelRequest);
    SaleChannel activateSaleChannel(@NonNull Long userId, @NonNull Long saleChannelId);
    SaleChannel deactivateSaleChannel(@NonNull Long userId, @NonNull Long saleChannelId);
    List<SaleChannel> activateSaleChannels(@NonNull Long userId, @NonNull List<Long> saleChannelIds);
    List<SaleChannel> deactivateSaleChannels(@NonNull Long userId, @NonNull List<Long> saleChannelIds);
}
