package com.wifosell.zeus.payload.response.ecom_sync;

import com.wifosell.zeus.model.ecom_sync.EcomAccount;
import com.wifosell.zeus.model.sale_channel.SaleChannel;
import com.wifosell.zeus.payload.response.BasicEntityResponse;

public class EcomAccountResponse extends BasicEntityResponse {
    private final String accountName;
    private final String shopName;
    private final String accountInfo;
    private final EcomAccount.EcomName ecomName;
    private final String description;
    private final EcomAccount.AccountStatus accountStatus;

    public EcomAccountResponse(EcomAccount ecomAccount) {
        super(ecomAccount);
        this.accountName = ecomAccount.getAccountName();
        this.shopName = ecomAccount.getShopName();
        this.accountInfo = ecomAccount.getAccountInfo();
        this.ecomName = ecomAccount.getEcomName();
        this.description = ecomAccount.getDescription();
        this.accountStatus = ecomAccount.getAccountStatus();
    }
}
