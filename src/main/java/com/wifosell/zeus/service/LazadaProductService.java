package com.wifosell.zeus.service;

import com.wifosell.zeus.model.ecom_sync.EcomAccount;
import com.wifosell.zeus.model.ecom_sync.LazadaProduct;
import com.wifosell.zeus.model.ecom_sync.LazadaVariant;
import com.wifosell.zeus.payload.provider.lazada.report.FetchLazadaProductsReport;
import com.wifosell.zeus.payload.provider.lazada.report.PushLazadaProductsReport;
import org.springframework.data.domain.Page;

public interface LazadaProductService {
    FetchLazadaProductsReport fetchLazadaProducts(Long userId, Long ecomId);

    PushLazadaProductsReport pushLazadaProducts(Long userId, Long ecomId);

    void updateLinkedLazadaProducts(Long userId, Long productId);

    boolean pushLazadaVariantQuantity(Long userId, Long ecomId, Long variantId);

    Page<LazadaProduct> getLazadaProducts(Long userId, Long ecomId, Integer offset, Integer limit, String sortBy, String orderBy);

    LazadaProduct getLazadaProduct(Long userId, Long id);

    Page<LazadaVariant> getLazadaVariants(Long userId, Long ecomId, Integer offset, Integer limit, String sortBy, String orderBy);

    LazadaVariant getLazadaVariant(Long userId, Long id);

    void autoFetchLazadaProducts(EcomAccount ecomAccount);
}
