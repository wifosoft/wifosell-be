package com.wifosell.zeus.service.impl.ecom_sync;

import com.google.gson.Gson;
import com.wifosell.zeus.exception.ZeusGlobalException;
import com.wifosell.zeus.model.ecom_sync.*;
import com.wifosell.zeus.model.warehouse.Warehouse;
import com.wifosell.zeus.payload.provider.shopee.ResponseSendoProductItemPayload;
import com.wifosell.zeus.payload.request.product.UpdateProductRequest;
import com.wifosell.zeus.repository.*;
import com.wifosell.zeus.repository.ecom_sync.*;
import com.wifosell.zeus.service.ProductService;
import com.wifosell.zeus.service.SendoProductService;
import com.wifosell.zeus.specs.SendoProductSpecs;
import com.wifosell.zeus.utils.ZeusUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.GeneralSecurityException;
import java.util.Optional;

@Service
@Transactional
public class SendoProductServiceImpl implements SendoProductService {

    private Logger logger = LoggerFactory.getLogger(SendoProductServiceImpl.class);
    @Autowired
    ProductService productService;
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    VariantRepository variantRepository;

    @Autowired
    OptionRepository optionRepository;

    @Autowired
    OptionValueRepository optionValueRepository;

    @Autowired
    LazadaSwwAndEcomAccountRepository lazadaSwwAndEcomAccountRepository;

    @Autowired
    SendoVariantRepository sendoVariantRepository;

    @Autowired
    SendoProductRepository sendoProductRepository;

    @Autowired
    EcomAccountRepository ecomAccountRepository;

    public Page<SendoProduct> getProducts(
            Long ecomId,
            int offset,
            int limit,
            String sortBy,
            String orderBy
    ) {
        Long ecomIdCk = ecomId == null ? null : ecomAccountRepository.getEcomAccountById(ecomId).getId();
        return sendoProductRepository.findAll(
                SendoProductSpecs.inEcomAccount(ecomIdCk),
                ZeusUtils.getDefaultPageable(offset, limit, sortBy, orderBy)
        );
    }

    @Override
    public Page<SendoVariant> getVariants(Long ecomId, int offset, int limit, String sortBy, String orderBy) {
        Long ecomIdCk = ecomId == null ? null : ecomAccountRepository.getEcomAccountById(ecomId).getId();
        return sendoVariantRepository.findAll(
                ZeusUtils.getDefaultPageable(offset, limit, sortBy, orderBy)
        );
    }


    public void consumeSingleSendoProductFromAPI(ResponseSendoProductItemPayload itemPayload) {
        //consume va luu vao db
        //userid productid,
        try {
            String ShopKey = itemPayload.getShop_relation_id();
            Optional<EcomAccount> ecomAccountOpt = ecomAccountRepository.findByAccountNameAndEcomName(ShopKey, EcomAccount.EcomName.SENDO);
            if (ecomAccountOpt.isEmpty()) {
                logger.info("Shop key khong ton tai " + ShopKey);
                return;
            }


            Optional<LazadaSwwAndEcomAccount> relationSwwEAOpt = lazadaSwwAndEcomAccountRepository.findByEcomAccountId(ecomAccountOpt.get().getId());
            if (relationSwwEAOpt.isEmpty()) {
                logger.info("Khong ton tai relation");
            } else {
                Warehouse warehouse = relationSwwEAOpt.get().getSaleChannelShop().getWarehouse();
            }


            UpdateProductRequest updateProductRequest = UpdateProductRequest.withResponseSendoProductItemPayload(itemPayload);
            logger.info((new Gson()).toJson(updateProductRequest));

            var response = productService.updateProduct(ecomAccountOpt.get().getGeneralManager().getId(), -1L, updateProductRequest);
            logger.info(response.getName());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
