package com.wifosell.zeus.service.impl.ecom_sync;

import com.google.gson.Gson;
import com.wifosell.zeus.consumer.payload.KafkaWrapperConsumeProduct;
import com.wifosell.zeus.consumer.payload.KafkaWrapperConsumeProductVariantShortInfo;
import com.wifosell.zeus.exception.ZeusGlobalException;
import com.wifosell.zeus.model.ecom_sync.*;
import com.wifosell.zeus.model.product.Product;
import com.wifosell.zeus.model.product.Variant;
import com.wifosell.zeus.model.stock.Stock;
import com.wifosell.zeus.model.user.User;
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
import java.math.BigDecimal;
import java.util.List;
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

    @Autowired
    SendoVariantAndSysVarirantRepository sendoVariantAndSysVarirantRepository;

    @Autowired
    StockRepository stockRepository;


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


    public void consumeSingleSendoProductLinkProductPhase(ResponseSendoProductItemPayload itemPayload){
        //created product, only mapping to db
        Optional<EcomAccount> ecomAccountOpt = ecomAccountRepository.findByAccountNameAndEcomName(itemPayload.getShop_relation_id(), EcomAccount.EcomName.SENDO);
        if (ecomAccountOpt.isEmpty()) {
            logger.info("Shop key khong ton tai " + itemPayload.getShop_relation_id());
            return;
        }

        Optional<LazadaSwwAndEcomAccount> swwAndEcomAccountOpt = lazadaSwwAndEcomAccountRepository.findByEcomAccountId(ecomAccountOpt.get().getId());
        if(swwAndEcomAccountOpt.isEmpty() ){
            logger.info("Chua ton tai lien ket shop");
            return;
        }
        LazadaSwwAndEcomAccount swwAndEcomAccount = swwAndEcomAccountOpt.get();

        Warehouse warehouse = swwAndEcomAccount.getSaleChannelShop().getWarehouse();

        User gm = ecomAccountOpt.get().getGeneralManager();


        SendoProduct sendoProduct = sendoProductRepository.findByItemId(itemPayload.getId());
        if(sendoProduct == null){
            sendoProduct = new SendoProduct(itemPayload, ecomAccountOpt.get());
        }
        else {
            sendoProduct.withDataByProductAPI(itemPayload).setEcomAccount(ecomAccountOpt.get());
        }
        logger.info("[+] Save sendo product {}" , sendoProduct.getName());

        sendoProductRepository.save(sendoProduct);

        List<ResponseSendoProductItemPayload.Variant> listAPIVariants = itemPayload.getVariants();

        boolean flagNewSendoVariantRecord =false;
        if(listAPIVariants.size() > 0 ) {
            for (var _apiVariant : listAPIVariants) {
                SendoVariant sendoVariant = sendoVariantRepository.findBySkuId(_apiVariant.getVariant_sku());
                if (sendoVariant == null) {
                    sendoVariant = new SendoVariant(_apiVariant, sendoProduct);
                    flagNewSendoVariantRecord =true;
                } else {
                    sendoVariant = sendoVariant.withDataBySkuAPI(_apiVariant);
                }
                sendoVariantRepository.save(sendoVariant);


                logger.info("[+] Save sendo variant SKU {} - Product Name {} ", sendoVariant.getSkuId(), sendoProduct.getName());
            }
        }else {
            SendoVariant sendoVariant = sendoVariantRepository.findBySkuId(itemPayload.getSku());
            if (sendoVariant == null) {
                sendoVariant = new SendoVariant(itemPayload, sendoProduct);
                flagNewSendoVariantRecord = true;
            } else {
                sendoVariant = sendoVariant.withSingleVariant(itemPayload);
            }
            sendoVariantRepository.save(sendoVariant);
            logger.info("[+] Save single sendo variant SKU {} - Product Name {} ", sendoVariant.getSkuId(), sendoProduct.getName());
        }
        //luu thnah cong 2 bang sendo_product, sendo_variant



        //them vao csdl product, sendo neu sku khong ton tai
        KafkaWrapperConsumeProduct kafkaWrapperConsumeProduct = UpdateProductRequest.withResponseSendoProductItemPayload(itemPayload);
        logger.info((new Gson()).toJson(kafkaWrapperConsumeProduct.getUpdateProductRequest()));


        var listMapVariantSkuString = kafkaWrapperConsumeProduct.mapVariantSkus();
        List<Variant> existedVariant = variantRepository.findListBySku(listMapVariantSkuString);
        Long idProductAffected = -1L;
        if(existedVariant.size() != 0){
            //ton tai system varirant roi thi cap nhat theo varaint id
            Product parentExist = existedVariant.get(0).getProduct();
            if(parentExist != null) {
                idProductAffected = parentExist.getId();
            }
        }

        if(idProductAffected == -1L){
            var response = productService.updateProduct(ecomAccountOpt.get().getGeneralManager().getId(), idProductAffected, kafkaWrapperConsumeProduct.getUpdateProductRequest());
            logger.info(response.getName());
            //nếu chưa tồn tại thì mới tạo mới thôi
        }
        else {
            //tồn tại rồi thì không cập nhật thông tin sản phẩm nữa
        }
        //create if not exist
        //

        //cap nhat price, stock

        for(KafkaWrapperConsumeProductVariantShortInfo skuInfoInstance : kafkaWrapperConsumeProduct.getListVariants()){
            Variant _systemVar = variantRepository.getBySKUNoThrow(skuInfoInstance.getSku() ,gm.getId());

            SendoVariant _sendoVar = sendoVariantRepository.findBySkuId(skuInfoInstance.getSku());

            if(_systemVar != null && _sendoVar !=null){
                //check liên kết, nếu chưa liên kết thì liên kết. Sau đó cập nhật stock và quantity
                _systemVar.setCost(new BigDecimal(skuInfoInstance.getPrice()));
                _systemVar.setOriginalCost(new BigDecimal(skuInfoInstance.getPrice()));

                variantRepository.save(_systemVar);
                //check warehouse nếu chưa liên kết thì liên kết

                SendoVariantAndSysVariant sendoVariantAndSysVariant = sendoVariantAndSysVarirantRepository.findFirstBySendoVariantSysVariant(_sendoVar.getId(), _systemVar.getId());
                if(sendoVariantAndSysVariant ==null) {
                    sendoVariantAndSysVariant =  new SendoVariantAndSysVariant();
                }
                sendoVariantAndSysVariant.setSendoVariant(_sendoVar);
                sendoVariantAndSysVariant.setVariant( _systemVar);
                sendoVariantAndSysVarirantRepository.save(sendoVariantAndSysVariant);
                logger.info("[+] Link sendo product and system product : {} vs {}", _sendoVar.getId(), _systemVar.getId());
                Optional<Stock> stock_ = stockRepository.findByVariantAndWarehouse(_systemVar.getId(), warehouse.getId());

                if (stock_.isPresent()) {
                    Stock stock = stock_.get();
                    stock.setQuantity(skuInfoInstance.getQuantity().intValue());
                    stock.setActualQuantity(skuInfoInstance.getQuantity().intValue());
                    stockRepository.save(stock);
                    logger.info("[+] Update stock : {} - stock : {}",  _systemVar.getId(), stock.getQuantity());

                } else {
                    //chưa tồn tại stock warehouse thì thêm record
                    Stock stock = new Stock();
                    stock.setQuantity(skuInfoInstance.getQuantity().intValue());
                    stock.setActualQuantity(skuInfoInstance.getQuantity().intValue());
                    stock.setVariant(_systemVar);
                    stock.setWarehouse(warehouse);
                    stockRepository.save(stock);
                    logger.info("[+] Create stock : {} - stock : {}",  _systemVar.getId(), stock.getQuantity());
                }

            }
        }



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


            this.consumeSingleSendoProductLinkProductPhase(itemPayload);
            logger.info("[+] After consume consumeSingleSendoProductLinkProductPhase {}", itemPayload.getName());



        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
