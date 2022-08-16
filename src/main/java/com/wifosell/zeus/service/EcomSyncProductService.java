package com.wifosell.zeus.service;

import com.wifosell.zeus.payload.request.ecom_sync.EcomSyncUpdateStockRequest;

public interface EcomSyncProductService {
    boolean updateStock(Long userId, EcomSyncUpdateStockRequest request);
}
