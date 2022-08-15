package com.wifosell.zeus.service;

import com.wifosell.zeus.model.ecom_sync.SendoProduct;
import com.wifosell.zeus.model.ecom_sync.SendoVariant;
import com.wifosell.zeus.payload.provider.shopee.ResponseSendoProductItemPayload;
import com.wifosell.zeus.payload.request.ecom_sync.SendoCreateOrUpdateProductPayload;
import org.springframework.data.domain.Page;

public interface SendoProductService {
    Page<SendoProduct> getProducts(
            Long ecomId,
            int offset,
            int limit,
            String sortBy,
            String orderBy
    );

    Page<SendoVariant> getVariants(
            Long ecomId,
            int offset,
            int limit,
            String sortBy,
            String orderBy
    );

    void consumeSingleSendoProductFromAPI(ResponseSendoProductItemPayload itemPayload);
    SendoCreateOrUpdateProductPayload pulishCreateSystemProductToSendo(Long ecomId, Long sysProductId);

    SendoCreateOrUpdateProductPayload postNewProductToSendo(Long ecomId, Long sysProductId);

    boolean postAllProductToSendo(Long ecomId);


    boolean fetchAndSyncSendoProducts( Long ecomId);



}
