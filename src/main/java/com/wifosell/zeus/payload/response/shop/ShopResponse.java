package com.wifosell.zeus.payload.response.shop;

import com.wifosell.zeus.model.shop.SaleChannelShop;
import com.wifosell.zeus.model.shop.Shop;
import com.wifosell.zeus.payload.response.BasicEntityResponse;
import com.wifosell.zeus.payload.response.sale_channel.SaleChannelResponse;
import com.wifosell.zeus.payload.response.warehouse.WarehouseResponse;
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
    private final List<RelationResponse> relations;

    public ShopResponse(Shop shop) {
        super(shop);
        this.name = shop.getName();
        this.shortName = shop.getShortName();
        this.address = shop.getAddress();
        this.phone = shop.getPhone();
        this.description = shop.getDescription();
        this.businessLine = shop.getBusinessLine();
        this.relations = shop.getSaleChannelShops().stream().map(RelationResponse::new).collect(Collectors.toList());
    }

    @Getter
    private static class RelationResponse {
        private final SaleChannelResponse saleChannel;
        private final WarehouseResponse warehouse;

        public RelationResponse(SaleChannelShop saleChannelShop) {
            this.saleChannel = new SaleChannelResponse(saleChannelShop.getSaleChannel());
            this.warehouse = new WarehouseResponse(saleChannelShop.getWarehouse());
        }
    }
}
