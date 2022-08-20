package com.wifosell.zeus.service.impl.ecom_sync;

import com.wifosell.zeus.model.ecom_sync.EcomAccount;
import com.wifosell.zeus.model.ecom_sync.LazadaSwwAndEcomAccount;
import com.wifosell.zeus.model.product.Variant;
import com.wifosell.zeus.model.shop.SaleChannelShop;
import com.wifosell.zeus.model.warehouse.Warehouse;
import com.wifosell.zeus.payload.request.ecom_sync.EcomSyncUpdateStockRequest;
import com.wifosell.zeus.payload.response.ecom_sync.EcomSyncUpdateStockResponse;
import com.wifosell.zeus.repository.SaleChannelShopRepository;
import com.wifosell.zeus.repository.VariantRepository;
import com.wifosell.zeus.repository.ecom_sync.LazadaSwwAndEcomAccountRepository;
import com.wifosell.zeus.service.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service("EcomSyncProductService")
@Transactional
@RequiredArgsConstructor
public class EcomSyncProductServiceImpl implements EcomSyncProductService {
    private static final Logger logger = LoggerFactory.getLogger(EcomSyncProductServiceImpl.class);

    private final StockService stockService;
    private final VariantService variantService;
    private final VariantRepository variantRepository;
    private final LazadaSwwAndEcomAccountRepository lazadaSwwAndEcomAccountRepository;
    private final LazadaProductService lazadaProductService;
    private final SendoProductService sendoProductService;
    private final EcomService ecomService;
    private final SaleChannelShopRepository saleChannelShopRepository;


    void hookUpdateByVariant(Long variantId) {

    }


    @Override
    public void hookUpdateSendoProduct(Long ecomId, Long productId) {
        try {
            logger.info("[+] Enter Hook hookUpdateSendoProduct update price to sendo EcomId {} - ProductId {}", ecomId, productId);
            sendoProductService.publishCreateSystemProductToSendo(ecomId, productId);
            logger.info("[+] Send hookUpdateSendoProduct update price to sendo EcomId {} - ProductId {}", ecomId, productId);
        } catch (Exception exception) {
            exception.printStackTrace();
            logger.error("[-] Exception hookUpdateSendoProduct when send update to sendo");
        }
    }


    @Override
    public EcomSyncUpdateStockResponse updateStock(Long userId, EcomSyncUpdateStockRequest request) {
        LazadaSwwAndEcomAccount link = lazadaSwwAndEcomAccountRepository.getByEcomAccountId(request.getEcomId());
        Warehouse warehouse = link.getSaleChannelShop().getWarehouse();
        Variant variant = variantService.getVariant(userId, request.getVariantId());

        // Update system
        stockService.updateStock(warehouse, variant, request.getStock(), request.getStock());

        // Broadcast to Lazada and Sendo
        int lazadaTotal = 0;
        int lazadaSuccess = 0;
        int sendoTotal = 0;
        int sendoSuccess = 0;
        int offlineTotal = 0;

        List<SaleChannelShop> saleChannelShops = saleChannelShopRepository.findListSSWByWarehouseId(warehouse.getId());

        for (SaleChannelShop ssw : saleChannelShops) {
            List<LazadaSwwAndEcomAccount> sswLinks = lazadaSwwAndEcomAccountRepository.findAllBySaleChannelShopId(ssw.getId());

            for (LazadaSwwAndEcomAccount sswLink : sswLinks) {
                EcomAccount ecomAccount = sswLink.getEcomAccount();
                switch (ecomAccount.getEcomName()) {
                    case LAZADA:
                        boolean success = lazadaProductService.pushLazadaVariantQuantity(userId, ecomAccount.getId(), variant.getId());
                        if (success) {
                            lazadaSuccess++;
                            logger.info("[+] updateStock LAZADA success | userId = {}, ecomId = {}, variantId = {}",
                                    userId, ecomAccount.getId(), variant.getId());
                        } else {
                            logger.error("[-] updateStock LAZADA fail | userId = {}, ecomId = {}, variantId = {}",
                                    userId, ecomAccount.getId(), variant.getId());
                        }
                        lazadaTotal++;
                        break;
                    case SENDO:
                        Long productId = variant.getProduct().getId();
                        try {
                            sendoProductService.postNewProductToSendo(ecomAccount.getId(), productId);
                            sendoSuccess++;
                            logger.info("[+] updateStock SENDO success | userId = {}, ecomId = {}, variantId = {}, productId = {}",
                                    userId, ecomAccount.getId(), variant.getId(), productId);
                        } catch (Exception exception) {
                            exception.printStackTrace();
                            logger.error("[-] updateStock SENDO fail | userId = {}, exception | ecomId = {}, variantId = {}, productId = {}",
                                    userId, ecomAccount.getId(), variant.getId(), productId);
                        }
                        sendoTotal++;
                        break;
                    case TIKI:
                        logger.error("[-] updateStock TIKI not support | userId = {}", userId);
                        break;
                    case SHOPEE:
                        logger.error("[-] updateStock SHOPEE not support | userId = {}", userId);
                        break;
                    default:
                        logger.error("[-] updateStock fail | unknown ecomName | userId = {}, ecomName = {}",
                                userId, ecomAccount.getEcomName());
                }
            }
        }

        return EcomSyncUpdateStockResponse.builder()
                .lazadaTotal(lazadaTotal)
                .lazadaSuccess(lazadaSuccess)
                .sendoTotal(sendoTotal)
                .sendoSuccess(sendoSuccess)
                .offlineTotal(offlineTotal)
                .build();
    }
}
