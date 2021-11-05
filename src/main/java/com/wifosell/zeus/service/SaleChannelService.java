package com.wifosell.zeus.service;

import com.wifosell.zeus.model.salechannel.SaleChannel;
import com.wifosell.zeus.payload.request.salechannel.SaleChannelRequest;

import java.util.List;


public interface SaleChannelService {
    List<SaleChannel> getAllSaleChannel();

    SaleChannel addSaleChannel(Long userId, SaleChannelRequest saleChannelRequest);
    SaleChannel getSaleChannel(Long saleChannelId);
    SaleChannel updateSaleChannel(Long saleChannelId, SaleChannelRequest saleChannelRequest);
    SaleChannel activateSaleChannel(Long saleChannelId);
    SaleChannel deActivateSaleChannel(Long saleChannelId);


}
