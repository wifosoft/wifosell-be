package com.wifosell.lazada.modules.category;

import com.lazada.lazop.api.LazopRequest;
import com.lazada.lazop.api.LazopResponse;
import com.lazada.lazop.util.ApiException;
import com.wifosell.lazada.constant.LazadaAPIConstant;
import com.wifosell.lazada.modules.category.payload.LazadaGetCategoryAttributesResponse;
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
        request.addApiParameter("language_code", LazadaAPIConstant.DEFAULT_LANGUAGE_CODE);

        LazopResponse response = LazadaClient.getClient().execute(request);

        if (!response.isSuccess()) {
            logger.error("getCategoryTree fail | body = {}", response.getBody());
        }

        return LazadaConvertUtils.fromJsonStringToObject(response.getBody(), LazadaGetCategoryTreeResponse.class);
    }

    public static LazadaGetCategoryAttributesResponse getCategoryAttributes(Long categoryId) throws ApiException {
        LazopRequest request = new LazopRequest();

        request.setApiName("/category/attributes/get");
        request.setHttpMethod("GET");
        request.addApiParameter("primary_category_id", String.valueOf(categoryId));
        request.addApiParameter("language_code", LazadaAPIConstant.DEFAULT_LANGUAGE_CODE);

        LazopResponse response = LazadaClient.getClient().execute(request);

        if (!response.isSuccess()) {
            logger.error("getCategoryAttributes fail | categoryId = {}, body = {}", categoryId, response.getBody());
            return null;
        }

        logger.info("getCategoryAttributes success | categoryId = {}", categoryId);
        return LazadaGetCategoryAttributesResponse.fromJson(response.getBody());
    }
}
