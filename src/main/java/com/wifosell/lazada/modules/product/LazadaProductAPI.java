package com.wifosell.lazada.modules.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lazada.lazop.api.LazopRequest;
import com.lazada.lazop.api.LazopResponse;
import com.lazada.lazop.util.ApiException;
import com.wifosell.lazada.modules.product.payload.LazadaUpdatePriceQuantityRequest;
import com.wifosell.zeus.taurus.lazada.LazadaClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LazadaProductAPI {
    private static final Logger logger = LoggerFactory.getLogger(LazadaProductAPI.class.getSimpleName());

    public static boolean updatePriceAndQuantity(String accessToken, LazadaUpdatePriceQuantityRequest payload)
            throws JsonProcessingException, ApiException {
        LazopRequest request = new LazopRequest();

        request.setApiName("/product/price_quantity/update");
        request.addApiParameter("payload", payload.toXmlString());

        LazopResponse response = LazadaClient.getClient().execute(request, accessToken);

        if (!response.isSuccess()) {
            logger.warn("updatePriceAndQuantity fail | body = {}", response.getBody());
        }

        return response.isSuccess();
    }
}
