package com.wifosell.zeus.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lazada.lazop.util.ApiException;
import com.wifosell.zeus.model.ecom_sync.EcomAccount;
import com.wifosell.zeus.model.ecom_sync.LazadaProduct;
import com.wifosell.zeus.model.ecom_sync.LazadaVariant;
import com.wifosell.zeus.model.product.Product;
import com.wifosell.zeus.model.warehouse.Warehouse;
import com.wifosell.zeus.payload.provider.lazada.report.FetchLazadaProductsReport;
import com.wifosell.zeus.payload.provider.lazada.report.PushLazadaProductsReport;
import org.springframework.data.domain.Page;

public interface LazadaProductService {
    FetchLazadaProductsReport fetchLazadaProducts(Long userId, Long ecomId);

    PushLazadaProductsReport pushLazadaProducts(Long userId, Long ecomId);

    Long createLazadaProductItem(EcomAccount ecomAccount, Product sysProduct, Warehouse warehouse) throws JsonProcessingException, ApiException;

    Long updateLazadaProductItem(EcomAccount ecomAccount, Product sysProduct, Warehouse warehouse) throws JsonProcessingException, ApiException;

    boolean pushLazadaVariantQuantity(Long userId, Long ecomId, Long variantId);

    Page<LazadaProduct> getLazadaProducts(Long userId, Long ecomId, Integer offset, Integer limit, String sortBy, String orderBy);

    LazadaProduct getLazadaProduct(Long userId, Long id);

    Page<LazadaVariant> getLazadaVariants(Long userId, Long ecomId, Integer offset, Integer limit, String sortBy, String orderBy);

    LazadaVariant getLazadaVariant(Long userId, Long id);
}
