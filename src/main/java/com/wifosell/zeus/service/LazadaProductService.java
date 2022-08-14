package com.wifosell.zeus.service;

import com.lazada.lazop.util.ApiException;
import com.wifosell.zeus.model.ecom_sync.LazadaProduct;
import com.wifosell.zeus.model.ecom_sync.LazadaVariant;
import com.wifosell.zeus.payload.provider.lazada.report.FetchAndSyncLazadaProductsReport;
import org.springframework.data.domain.Page;

public interface LazadaProductService {
    FetchAndSyncLazadaProductsReport fetchAndSyncLazadaProducts(Long userId, Long ecomId) throws ApiException;

    Page<LazadaProduct> getLazadaProducts(
            Long ecomId,
            int offset,
            int limit,
            String sortBy,
            String orderBy
    );

    Page<LazadaVariant> getLazadaVariants(
            Long ecomId,
            int offset,
            int limit,
            String sortBy,
            String orderBy
    );
}
