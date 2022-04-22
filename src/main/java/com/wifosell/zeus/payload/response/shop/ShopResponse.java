package com.wifosell.zeus.payload.response.shop;

import com.wifosell.zeus.model.sale_channel.SaleChannel;
import com.wifosell.zeus.model.shop.SaleChannelShopRelation;
import com.wifosell.zeus.model.shop.Shop;
import com.wifosell.zeus.payload.response.BasicEntityResponse;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ShopResponse extends BasicEntityResponse {
    private final String name;
    private final String shortName;
    private final String address;
    private final String phone;
    private final String description;
    private final String businessLine;
    private final List<SaleChannelResponse> saleChannels;

    public ShopResponse(Shop shop) {
        super(shop);
        this.name = shop.getName();
        this.shortName = shop.getShortName();
        this.address = shop.getAddress();
        this.phone = shop.getPhone();
        this.description = shop.getDescription();
        this.businessLine = shop.getBusinessLine();
        this.saleChannels = shop.getSaleChannelShopRelations().stream()
                .map(SaleChannelShopRelation::getSaleChannel)
                .map(SaleChannelResponse::new).collect(Collectors.toList());
    }

    @Getter
    private static class SaleChannelResponse extends BasicEntityResponse {
        private final String name;
        private final String shortName;
        private final String description;

        SaleChannelResponse(SaleChannel saleChannel) {
            super(saleChannel);
            this.name = saleChannel.getName();
            this.shortName = saleChannel.getShortName();
            this.description = saleChannel.getDescription();
        }
    }
}
