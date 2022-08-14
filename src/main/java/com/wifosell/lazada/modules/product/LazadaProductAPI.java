package com.wifosell.lazada.modules.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lazada.lazop.api.LazopRequest;
import com.lazada.lazop.api.LazopResponse;
import com.lazada.lazop.util.ApiException;
import com.wifosell.lazada.modules.category.LazadaCategoryAPI;
import com.wifosell.lazada.modules.category.payload.LazadaGetCategoryAttributesResponse;
import com.wifosell.lazada.modules.product.payload.LazadaGetProductItemResponse;
import com.wifosell.lazada.modules.product.payload.LazadaGetProductsResponse;
import com.wifosell.lazada.modules.product.payload.LazadaUpdatePriceQuantityRequest;
import com.wifosell.zeus.taurus.lazada.LazadaClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LazadaProductAPI {
    private static final Logger logger = LoggerFactory.getLogger(LazadaProductAPI.class);

    public static LazadaGetProductsResponse getProducts(String accessToken, int offset, int limit) throws ApiException {
        LazopRequest request = new LazopRequest();

        request.setApiName("/products/get");
        request.setHttpMethod("GET");
        request.addApiParameter("filter", "live");
        request.addApiParameter("update_before", "");
        request.addApiParameter("create_before", "");
        request.addApiParameter("offset", String.valueOf(offset));
        request.addApiParameter("create_after", "");
        request.addApiParameter("update_after", "");
        request.addApiParameter("limit", String.valueOf(limit));
        request.addApiParameter("options", "1");
        request.addApiParameter("sku_seller_list", "");

        LazopResponse response = LazadaClient.getClient().execute(request, accessToken);

        if (!response.isSuccess()) {
            logger.error("getProducts fail | body = {}.", response.getBody());
            return null;
        }

        logger.info("getProducts success.");
        return LazadaGetProductsResponse.fromJson(response.getBody());
    }

    public static LazadaGetProductItemResponse getProductItem(String accessToken, Long productId) throws ApiException {
        LazopRequest request = new LazopRequest();

        request.setApiName("/product/item/get");
        request.setHttpMethod("GET");
        request.addApiParameter("item_id", String.valueOf(productId));
        request.addApiParameter("seller_sku", "");

        LazopResponse response = LazadaClient.getClient().execute(request, accessToken);

        if (!response.isSuccess()) {
            logger.error("getProductItem fail | productId = {}, body = {}.", productId, response.getBody());
            return null;
        }

        logger.info("getProductItem success | productId = {}.", productId);

        LazadaGetProductItemResponse productItemResponse = LazadaGetProductItemResponse.fromJson(response.getBody());

        LazadaGetCategoryAttributesResponse attributesRes = LazadaCategoryAPI.getCategoryAttributes(productItemResponse.getData().getPrimaryCategoryId());

        if (attributesRes != null) {
            productItemResponse.translateData(attributesRes);
        }

        return productItemResponse;
    }

    public static boolean updatePriceAndQuantity(String accessToken, LazadaUpdatePriceQuantityRequest payload) throws JsonProcessingException, ApiException {
        LazopRequest request = new LazopRequest();

        request.setApiName("/product/price_quantity/update");
        request.addApiParameter("payload", payload.toXmlString());

        LazopResponse response = LazadaClient.getClient().execute(request, accessToken);

        if (!response.isSuccess()) {
            logger.error("updatePriceAndQuantity fail | body = {}.", response.getBody());
            return false;
        }

        logger.info("updatePriceAndQuantity success.");
        return true;
    }
}
