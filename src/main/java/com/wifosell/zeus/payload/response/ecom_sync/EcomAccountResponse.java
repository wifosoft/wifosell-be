package com.wifosell.zeus.payload.response.ecom_sync;

import com.wifosell.zeus.model.ecom_sync.EcomAccount;
import com.wifosell.zeus.model.sale_channel.SaleChannel;
import com.wifosell.zeus.model.shop.Shop;
import com.wifosell.zeus.model.warehouse.Warehouse;
import com.wifosell.zeus.payload.response.BasicEntityResponse;
import com.wifosell.zeus.payload.response.shop.ShopResponse;
import com.wifosell.zeus.payload.response.shop.ShopResponseNoRelation;
import com.wifosell.zeus.payload.response.warehouse.WarehouseResponse;
import com.wifosell.zeus.repository.ShopRepository;
import lombok.Getter;

@Getter
public class EcomAccountResponse extends BasicEntityResponse {
    private final String accountName;
    private final String shopName;
    private final String accountInfo;
    private final EcomAccount.EcomName ecomName;
    private final String description;
    private final EcomAccount.AccountStatus accountStatus;
    private final WarehouseResponse warehouseLinked;
    private final ShopResponseNoRelation shopeLinked;

    public EcomAccountResponse(EcomAccount ecomAccount) {
        super(ecomAccount);
        this.accountName = ecomAccount.getAccountName();
        this.shopName = ecomAccount.getShopName();
        this.accountInfo = ecomAccount.getAccountInfo();
        this.ecomName = ecomAccount.getEcomName();
        this.description = ecomAccount.getDescription();
        this.accountStatus = ecomAccount.getAccountStatus();

        if (ecomAccount.getRelationSwws() != null)
        {
            this.warehouseLinked = new WarehouseResponse( ecomAccount.getRelationSwws().getSaleChannelShop().getWarehouse());
            this.shopeLinked = new ShopResponseNoRelation(ecomAccount.getRelationSwws().getSaleChannelShop().getShop());
        }
        else {
            warehouseLinked = null;
            shopeLinked = null;
        }
    }
}
