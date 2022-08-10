package com.wifosell.zeus.service;

import com.wifosell.zeus.model.ecom_sync.LazadaProduct;
import com.wifosell.zeus.model.ecom_sync.LazadaVariant;
import com.wifosell.zeus.model.ecom_sync.SendoProduct;
import com.wifosell.zeus.model.ecom_sync.SendoVariant;
import com.wifosell.zeus.payload.provider.shopee.ResponseSendoProductItemPayload;
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
}
