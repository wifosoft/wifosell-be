package com.wifosell.zeus.service.impl.ecom_sync;

import com.wifosell.zeus.model.ecom_sync.LazadaSwwAndEcomAccount;
import com.wifosell.zeus.model.product.Variant;
import com.wifosell.zeus.model.warehouse.Warehouse;
import com.wifosell.zeus.payload.request.ecom_sync.EcomSyncUpdateStockRequest;
import com.wifosell.zeus.repository.ecom_sync.LazadaSwwAndEcomAccountRepository;
import com.wifosell.zeus.service.EcomSyncProductService;
import com.wifosell.zeus.service.LazadaProductService;
import com.wifosell.zeus.service.StockService;
import com.wifosell.zeus.service.VariantService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service("EcomSyncProductService")
@Transactional
@RequiredArgsConstructor
public class EcomSyncProductServiceImpl implements EcomSyncProductService {
    private static final Logger logger = LoggerFactory.getLogger(EcomSyncProductServiceImpl.class);

    private final StockService stockService;
    private final VariantService variantService;
    private final LazadaSwwAndEcomAccountRepository lazadaSwwAndEcomAccountRepository;
    private final LazadaProductService lazadaProductService;

    @Override
    public boolean updateStock(Long userId, EcomSyncUpdateStockRequest request) {
        // Update system
        LazadaSwwAndEcomAccount link = lazadaSwwAndEcomAccountRepository.getByEcomAccountId(request.getEcomId());
        Warehouse warehouse = link.getSaleChannelShop().getWarehouse();
        Variant variant = variantService.getVariant(userId, request.getVariantId());
        stockService.updateStock(warehouse, variant, request.getStock(), request.getStock());

        // Update Lazada
        boolean lazadaSuccess = lazadaProductService.pushLazadaVariantQuantity(userId, request.getEcomId(), variant.getId());

        // Update Sendo
        // TODO

        return true;
    }
}
