package com.wifosell.zeus.payload.response.ecom_sync;

import com.wifosell.zeus.model.ecom_sync.EcomAccount;
import com.wifosell.zeus.payload.response.BasicEntityResponse;
import com.wifosell.zeus.payload.response.shop.ShopResponseNoRelation;
import com.wifosell.zeus.payload.response.warehouse.WarehouseResponse;
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
    private final ShopResponseNoRelation shopLinked;

    public EcomAccountResponse(EcomAccount ecomAccount) {
        super(ecomAccount);
        this.accountName = ecomAccount.getAccountName();
        this.shopName = ecomAccount.getShopName();
        this.accountInfo = ecomAccount.getAccountInfo();
        this.ecomName = ecomAccount.getEcomName();
        this.description = ecomAccount.getDescription();
        this.accountStatus = ecomAccount.getAccountStatus();

        if (ecomAccount.getRelationSwws() != null) {
            this.warehouseLinked = new WarehouseResponse(ecomAccount.getRelationSwws().getSaleChannelShop().getWarehouse());
            this.shopLinked = new ShopResponseNoRelation(ecomAccount.getRelationSwws().getSaleChannelShop().getShop());
        } else {
            warehouseLinked = null;
            shopLinked = null;
        }
    }
}
