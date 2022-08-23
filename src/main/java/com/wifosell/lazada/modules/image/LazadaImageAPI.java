package com.wifosell.lazada.modules.image;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lazada.lazop.api.LazopRequest;
import com.lazada.lazop.api.LazopResponse;
import com.lazada.lazop.util.ApiException;
import com.wifosell.lazada.modules.image.payload.LazadaMigrateImagesBatchResponse;
import com.wifosell.lazada.modules.image.payload.LazadaMigrateImagesRequest;
import com.wifosell.lazada.modules.image.payload.LazadaMigrateImagesResponse;
import com.wifosell.zeus.taurus.lazada.LazadaClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LazadaImageAPI {
    private static final Logger logger = LoggerFactory.getLogger(LazadaImageAPI.class);

    public static LazadaMigrateImagesBatchResponse migrateImages(String accessToken, LazadaMigrateImagesRequest payload) throws JsonProcessingException, ApiException {
        LazopRequest request = new LazopRequest();

        request.setApiName("/images/migrate");
        request.addApiParameter("payload", payload.toXml());

        LazopResponse response = LazadaClient.getClient().execute(request, accessToken);

        if (!response.isSuccess()) {
            logger.error("migrateImages fail | body = {}", response.getBody());
            return null;
        }

        logger.info("migrateImages success");
        return LazadaMigrateImagesBatchResponse.fromJson(response.getBody());
    }

    public static LazadaMigrateImagesResponse getMigrateImages(String accessToken, String batchId) throws ApiException {
        LazopRequest request = new LazopRequest();

        request.setApiName("/image/response/get");
        request.setHttpMethod("GET");
        request.addApiParameter("batch_id", batchId);

        LazopResponse response = LazadaClient.getClient().execute(request, accessToken);

        if (!response.isSuccess()) {
            logger.error("getMigrateImages fail | body = {}", response.getBody());
            return null;
        }

        LazadaMigrateImagesResponse migrateImagesResponse = LazadaMigrateImagesResponse.fromJson(response.getBody());

        logger.info("getMigrateImages success | imageCount = {}", migrateImagesResponse.getData().getImages().size());

        return migrateImagesResponse;
    }
}
