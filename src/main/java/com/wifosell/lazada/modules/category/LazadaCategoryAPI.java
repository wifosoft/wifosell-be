package com.wifosell.lazada.modules.category;

import com.lazada.lazop.api.LazopRequest;
import com.lazada.lazop.api.LazopResponse;
import com.lazada.lazop.util.ApiException;
import com.wifosell.lazada.modules.category.payload.LazadaGetCategoryTreeResponse;
import com.wifosell.lazada.utils.LazadaConvertUtils;
import com.wifosell.zeus.taurus.lazada.LazadaClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LazadaCategoryAPI {
    private static final Logger logger = LoggerFactory.getLogger(LazadaCategoryAPI.class.getSimpleName());

    public static LazadaGetCategoryTreeResponse getCategoryTree() throws ApiException {
        LazopRequest request = new LazopRequest();

        request.setApiName("/category/tree/get");
        request.setHttpMethod("GET");
        request.addApiParameter("language_code", "vi_VN");

        LazopResponse response = LazadaClient.getClient().execute(request);

        if (!response.isSuccess()) {
            logger.warn("getCategoryTree fail | body = {}", response.getBody());
        }

        return LazadaConvertUtils.fromJsonStringToObject(response.getBody(), LazadaGetCategoryTreeResponse.class);
    }
}
