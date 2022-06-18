package com.wifosell.zeus.service;

import com.wifosell.zeus.model.ecom_sync.LazadaProduct;
import com.wifosell.zeus.model.ecom_sync.LazadaVariant;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

public interface LazadaProductService {
    Page<LazadaProduct> getProducts(
            Long ecomId,
            int offset,
            int limit,
            String sortBy,
            String orderBy
    );

    Page<LazadaVariant> getVariants(
            Long ecomId,
            int offset,
            int limit,
            String sortBy,
            String orderBy
    );

}
