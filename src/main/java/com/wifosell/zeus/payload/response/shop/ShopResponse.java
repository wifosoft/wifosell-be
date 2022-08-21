package com.wifosell.zeus.payload.response.shop;

import com.wifosell.zeus.model.ecom_sync.EcomAccount;
import com.wifosell.zeus.model.ecom_sync.LazadaSwwAndEcomAccount;
import com.wifosell.zeus.model.shop.SaleChannelShop;
import com.wifosell.zeus.model.shop.Shop;
import com.wifosell.zeus.payload.response.BasicEntityResponse;
import com.wifosell.zeus.payload.response.ecom_sync.EcomAccountResponse;
import com.wifosell.zeus.payload.response.sale_channel.SaleChannelResponse;
import com.wifosell.zeus.payload.response.warehouse.WarehouseResponse;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

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

        ArrayList<RelationResponse> listRelationResponse = new ArrayList<>();
        for (var ssw : shop.getSaleChannelShops()) {
            List<LazadaSwwAndEcomAccount> listLinkSwwECom = ssw.getLazadaSwwAndEcomAccount();
            for (var accountEcom : listLinkSwwECom) {
                RelationResponse rel = new RelationResponse(ssw, accountEcom.getEcomAccount(), accountEcom.getId());
                listRelationResponse.add(rel);
            }
        }
        this.relations = listRelationResponse;
    }


    @Getter
    private static class RelationResponse {
        private final Long sswId;
        private final Long sswEcomRelationId;
        private final SaleChannelResponse saleChannel;
        private final WarehouseResponse warehouse;
        private final EcomAccountResponse ecomAccount;

        public RelationResponse(SaleChannelShop saleChannelShop, EcomAccount ecomAccount, Long sswEcomRelationId) {
            this.saleChannel = new SaleChannelResponse(saleChannelShop.getSaleChannel());
            this.warehouse = new WarehouseResponse(saleChannelShop.getWarehouse());
            this.ecomAccount = new EcomAccountResponse(ecomAccount);
            this.sswId = saleChannelShop.getId();
            this.sswEcomRelationId = sswEcomRelationId;
        }
    }
}
