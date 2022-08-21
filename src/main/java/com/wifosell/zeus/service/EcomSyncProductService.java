package com.wifosell.zeus.service;

import com.wifosell.zeus.model.product.Variant;
import com.wifosell.zeus.model.warehouse.Warehouse;
import com.wifosell.zeus.payload.request.ecom_sync.EcomSyncUpdateStockRequest;
import com.wifosell.zeus.payload.response.ecom_sync.EcomSyncUpdateStockResponse;

public interface EcomSyncProductService {
    EcomSyncUpdateStockResponse updateStock(Long userId, EcomSyncUpdateStockRequest request);

    EcomSyncUpdateStockResponse updateEcomStock(Long userId, Warehouse warehouse, Variant variant);

    void hookUpdateSendoProduct(Long ecomId, Long productId);
}
