package com.wifosell.zeus.service;

import com.wifosell.zeus.payload.provider.shopee.hook.ResponseSendoOrderCreateHookPayload;

public interface SendoOrderService {

    void consumeOrderService(String shopKey, ResponseSendoOrderCreateHookPayload hookCreated);


}
