package com.wifosell.zeus.service;

import com.wifosell.zeus.model.sale_channel.SaleChannel;
import com.wifosell.zeus.payload.request.sale_channel.SaleChannelRequest;

import java.util.List;

public interface SaleChannelService {
    List<SaleChannel> getAllSaleChannels();
    List<SaleChannel> getSaleChannelsByUserId(Long userId);
    List<SaleChannel> getSaleChannelsByShopId(Long shopId);
    SaleChannel getSaleChannel(Long saleChannelId);
    SaleChannel addSaleChannel(Long userId, SaleChannelRequest saleChannelRequest);
    SaleChannel updateSaleChannel(Long saleChannelId, SaleChannelRequest saleChannelRequest);
    SaleChannel activateSaleChannel(Long saleChannelId);
    SaleChannel deactivateSaleChannel(Long saleChannelId);
}
