package com.wifosell.zeus.service;

import com.wifosell.zeus.model.ecom_sync.EcomAccount;
import com.wifosell.zeus.model.ecom_sync.SendoProduct;
import com.wifosell.zeus.model.ecom_sync.SendoVariant;
import com.wifosell.zeus.payload.provider.shopee.ResponseSendoProductItemPayload;
import com.wifosell.zeus.payload.provider.shopee.hook.ResponseSendoOrderCreateHookPayload;
import com.wifosell.zeus.payload.provider.shopee.hook.ResponseSendoOrderUpdateHookPayload;
import com.wifosell.zeus.payload.request.ecom_sync.SendoCreateOrUpdateProductPayload;
import org.springframework.data.domain.Page;

public interface SendoOrderService {

    void consumeOrderService(String shopKey,  ResponseSendoOrderCreateHookPayload hookCreated);
    


}
