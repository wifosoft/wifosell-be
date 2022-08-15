package com.wifosell.zeus.service.impl.ecom_sync;

import com.google.gson.Gson;
import com.wifosell.zeus.annotation.PreAuthorizeAccessGeneralManagerToShop;
import com.wifosell.zeus.config.property.AppProperties;
import com.wifosell.zeus.consumer.payload.KafkaWrapperConsumeProduct;
import com.wifosell.zeus.consumer.payload.KafkaWrapperConsumeProductVariantShortInfo;
import com.wifosell.zeus.exception.ZeusGlobalException;
import com.wifosell.zeus.model.ecom_sync.*;
import com.wifosell.zeus.model.product.Product;
import com.wifosell.zeus.model.product.Variant;
import com.wifosell.zeus.model.stock.Stock;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.model.warehouse.Warehouse;
import com.wifosell.zeus.payload.provider.shopee.ResponseLinkAccountPayload;
import com.wifosell.zeus.payload.provider.shopee.ResponseSendoProductItemPayload;
import com.wifosell.zeus.payload.provider.shopee.SendoServiceResponseBase;
import com.wifosell.zeus.payload.request.ecom_sync.SendoCreateOrUpdateProductPayload;
import com.wifosell.zeus.payload.request.ecom_sync.SendoLinkAccountRequestDTO;
import com.wifosell.zeus.payload.request.ecom_sync.SendoLinkAccountRequestDTOWithModel;
import com.wifosell.zeus.payload.request.product.UpdateProductRequest;
import com.wifosell.zeus.payload.response.product.ProductResponse;
import com.wifosell.zeus.repository.*;
import com.wifosell.zeus.repository.ecom_sync.*;
import com.wifosell.zeus.service.EcomService;
import com.wifosell.zeus.service.ProductService;
import com.wifosell.zeus.service.SendoProductService;
import com.wifosell.zeus.specs.ProductSpecs;
import com.wifosell.zeus.specs.SendoProductSpecs;
import com.wifosell.zeus.taurus.core.TaurusBus;
import com.wifosell.zeus.taurus.core.payload.KafkaPublishProductSendoPayload;
import com.wifosell.zeus.taurus.sendo.SendoServiceClient;
import com.wifosell.zeus.utils.ZeusUtils;
import org.apache.commons.compress.harmony.unpack200.Pack200UnpackerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.persistence.metamodel.ListAttribute;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class SendoProductServiceImpl implements SendoProductService {

    private Logger logger = LoggerFactory.getLogger(SendoProductServiceImpl.class);
    @Autowired
    SendoCategoryAndSysCategoryRepository sendoCategoryAndSysCategoryRepository;
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

    @Autowired
    AppProperties appProperties;

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    SendoProductService sendoProductService;

    @Autowired
    EcomService ecomService;


    public Page<SendoProduct> getProducts(
            Long ecomId,
            int offset,
            int limit,
            String sortBy,
            String orderBy
    ) {
        Long ecomIdCk = ecomId == null ? null : ecomAccountRepository.getById(ecomId).getId();
        return sendoProductRepository.findAll(
                SendoProductSpecs.inEcomAccount(ecomIdCk),
                ZeusUtils.getDefaultPageable(offset, limit, sortBy, orderBy)
        );
    }

    @Override
    public Page<SendoVariant> getVariants(Long ecomId, int offset, int limit, String sortBy, String orderBy) {
        Long ecomIdCk = ecomId == null ? null : ecomAccountRepository.getById(ecomId).getId();
        return sendoVariantRepository.findAll(
                ZeusUtils.getDefaultPageable(offset, limit, sortBy, orderBy)
        );
    }


    public void consumeSingleSendoProductLinkProductPhase(ResponseSendoProductItemPayload itemPayload) {
        //created product, only mapping to db
        Optional<EcomAccount> ecomAccountOpt = ecomAccountRepository.findByAccountNameAndEcomName(itemPayload.getShop_relation_id(), EcomAccount.EcomName.SENDO);
        if (ecomAccountOpt.isEmpty()) {
            logger.info("Shop key khong ton tai " + itemPayload.getShop_relation_id());
            return;
        }

        Optional<LazadaSwwAndEcomAccount> swwAndEcomAccountOpt = lazadaSwwAndEcomAccountRepository.findByEcomAccountId(ecomAccountOpt.get().getId());
        if (swwAndEcomAccountOpt.isEmpty()) {
            logger.info("Chua ton tai lien ket shop");
            return;
        }
        LazadaSwwAndEcomAccount swwAndEcomAccount = swwAndEcomAccountOpt.get();

        Warehouse warehouse = swwAndEcomAccount.getSaleChannelShop().getWarehouse();

        User gm = ecomAccountOpt.get().getGeneralManager();


        SendoProduct sendoProduct = sendoProductRepository.findByItemId(itemPayload.getId());
        if (sendoProduct == null) {
            sendoProduct = new SendoProduct(itemPayload, ecomAccountOpt.get());
        } else {
            sendoProduct.withDataByProductAPI(itemPayload).setEcomAccount(ecomAccountOpt.get());
        }
        logger.info("[+] Save sendo product {}", sendoProduct.getName());

        sendoProductRepository.save(sendoProduct);

        List<ResponseSendoProductItemPayload.Variant> listAPIVariants = itemPayload.getVariants();

        boolean flagNewSendoVariantRecord = false;
        if (listAPIVariants.size() > 0) {
            for (var _apiVariant : listAPIVariants) {
                SendoVariant sendoVariant = sendoVariantRepository.findBySkuId(_apiVariant.getVariant_sku());
                if (sendoVariant == null) {
                    sendoVariant = new SendoVariant(_apiVariant, sendoProduct);
                    flagNewSendoVariantRecord = true;
                } else {
                    sendoVariant = sendoVariant.withDataBySkuAPI(_apiVariant);
                }
                sendoVariantRepository.save(sendoVariant);
                logger.info("[+] Save sendo variant SKU {} - Product Name {} ", sendoVariant.getSkuId(), sendoProduct.getName());
            }
        } else {
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
        if (existedVariant.size() != 0) {
            //ton tai system varirant roi thi cap nhat theo varaint id
            Product parentExist = existedVariant.get(0).getProduct();
            if (parentExist != null) {
                idProductAffected = parentExist.getId();
            }
        }

        if (idProductAffected == -1L) {
            var response = productService.updateProduct(ecomAccountOpt.get().getGeneralManager().getId(), idProductAffected, kafkaWrapperConsumeProduct.getUpdateProductRequest());
            logger.info(response.getName());
            //nếu chưa tồn tại thì mới tạo mới thôi
        } else {
            //tồn tại rồi thì không cập nhật thông tin sản phẩm nữa
        }
        //create if not exist
        //

        //cap nhat price, stock

        for (KafkaWrapperConsumeProductVariantShortInfo skuInfoInstance : kafkaWrapperConsumeProduct.getListVariants()) {
            Variant _systemVar = variantRepository.getBySKUNoThrow(skuInfoInstance.getSku(), gm.getId());

            SendoVariant _sendoVar = sendoVariantRepository.findBySkuId(skuInfoInstance.getSku());

            if (_systemVar != null && _sendoVar != null) {
                //check liên kết, nếu chưa liên kết thì liên kết. Sau đó cập nhật stock và quantity
                _systemVar.setCost(new BigDecimal(skuInfoInstance.getPrice()));
                _systemVar.setOriginalCost(new BigDecimal(skuInfoInstance.getPrice()));

                variantRepository.save(_systemVar);
                //check warehouse nếu chưa liên kết thì liên kết

                SendoVariantAndSysVariant sendoVariantAndSysVariant = sendoVariantAndSysVarirantRepository.findFirstBySendoVariantSysVariant(_sendoVar.getId(), _systemVar.getId());
                if (sendoVariantAndSysVariant == null) {
                    sendoVariantAndSysVariant = new SendoVariantAndSysVariant();
                }
                sendoVariantAndSysVariant.setSendoVariant(_sendoVar);
                sendoVariantAndSysVariant.setVariant(_systemVar);
                sendoVariantAndSysVarirantRepository.save(sendoVariantAndSysVariant);
                logger.info("[+] Link sendo product and system product : {} vs {}", _sendoVar.getId(), _systemVar.getId());
                Optional<Stock> stock_ = stockRepository.findByVariantAndWarehouse(_systemVar.getId(), warehouse.getId());

                if (stock_.isPresent()) {
                    Stock stock = stock_.get();
                    stock.setQuantity(skuInfoInstance.getQuantity().intValue());
                    stock.setActualQuantity(skuInfoInstance.getQuantity().intValue());
                    stockRepository.save(stock);
                    logger.info("[+] Update stock : {} - stock : {}", _systemVar.getId(), stock.getQuantity());

                } else {
                    //chưa tồn tại stock warehouse thì thêm record
                    Stock stock = new Stock();
                    stock.setQuantity(skuInfoInstance.getQuantity().intValue());
                    stock.setActualQuantity(skuInfoInstance.getQuantity().intValue());
                    stock.setVariant(_systemVar);
                    stock.setWarehouse(warehouse);
                    stockRepository.save(stock);
                    logger.info("[+] Create stock : {} - stock : {}", _systemVar.getId(), stock.getQuantity());
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

    public SendoCreateOrUpdateProductPayload pulishCreateSystemProductToSendo(Long ecomId, Long sysProductId) {
        EcomAccount ecomAccount = ecomAccountRepository.getEcomAccountById(ecomId);
        if (ecomAccount == null) {
            logger.info("[-] Ecom Account Id {} not found", ecomId);
            return null;
        }
        User gm = ecomAccount.getGeneralManager();
        Product sysProduct = productService.getProduct(gm.getId(), sysProductId);
        if (sysProduct == null) {
            logger.info("[-] Product id {} not found ", sysProductId);
            return null;

        }

        // Lấy liên kết sww and ecom account
        Optional<LazadaSwwAndEcomAccount> linkSwwOpt = lazadaSwwAndEcomAccountRepository.findByEcomAccountId(ecomAccount.getId());
        if (linkSwwOpt.isEmpty()) {
            logger.info("[-] Chua lien ket ecom id {}, sys product {}, gm {}. ", ecomId, sysProductId, gm.getId());
            return null;
        }
        LazadaSwwAndEcomAccount linkSww = linkSwwOpt.get();

        Warehouse warehouseLinked = linkSww.getSaleChannelShop().getWarehouse();


        // Lấy liên kết category


        ProductResponse sysProductResponse = new ProductResponse(sysProduct);
        if(sysProduct.getCategory() == null){
            logger.info("[-] Khong co category ecom id {}, sys product {}, gm {} ", ecomId, sysProductId, gm.getId());
            return null;
        }

        Optional<SendoCategoryAndSysCategory> sendoCategoryAndSysCategoryOpt = sendoCategoryAndSysCategoryRepository.findByGeneralManagerIdAndSysCategoryId(gm.getId(), sysProductResponse.getCategory().getId());
        if (sendoCategoryAndSysCategoryOpt.isEmpty()) {
            logger.info("[-] Chua co lien ket category {}", sysProductResponse.getCategory().getId());
            return null;
        }
        SendoCategoryAndSysCategory sendoCategoryAndSysCategory = sendoCategoryAndSysCategoryOpt.get();

        //Xử lý ánh xạ sang payload

        SendoCreateOrUpdateProductPayload m = new SendoCreateOrUpdateProductPayload();
        m.setName(sysProductResponse.getName());

        List<Variant> variants = sysProduct.getVariants().stream()
                .filter(variant -> !variant.isDeleted()).collect(Collectors.toList());
        Variant firstVariant = variants.get(0);
        Integer firstStock = firstVariant.getStockWarehouse(warehouseLinked.getId());
        m.setSku(firstVariant.getSku()); //first sku
        m.setPrice(firstVariant.getCost().intValue());
        //lay stock moi nhat
        m.setStock_availability(firstStock > 0);
        m.setStock_quantity(firstStock);

        m.setDescription(sysProductResponse.getDescription());
        m.setCat_4_id(sendoCategoryAndSysCategory.getSendoCategory().getSendoCategoryId().intValue()); // category cần link cateogry trước

        //kich thuoc, trong luong...
        m.setHeight(sysProductResponse.getHeight().intValue());
        m.setLength(sysProductResponse.getLength().intValue());
        m.setWidth(sysProductResponse.getWidth().intValue());
        m.setWeight(sysProductResponse.getWeight().intValue());
        m.setUnit_id(1);

        //Avatar pic
        SendoCreateOrUpdateProductPayload.Avatar avatar = new SendoCreateOrUpdateProductPayload.Avatar();
        avatar.setPicture_url(sysProductResponse.getImages().get(0).getUrl());
        m.setAvatar(avatar);

        ArrayList<SendoCreateOrUpdateProductPayload.Picture> listPicture = new ArrayList<>();
        for (var _sysImage : sysProductResponse.getImages()) {
            SendoCreateOrUpdateProductPayload.Picture pic = new SendoCreateOrUpdateProductPayload.Picture();
            pic.setPicture_url(_sysImage.getUrl());
            listPicture.add(pic);
        }
        m.setPictures(listPicture);

        //
        m.set_config_variant(variants.size() > 1);
        SendoCreateOrUpdateProductPayload.Voucher voucher = new SendoCreateOrUpdateProductPayload.Voucher();
        voucher.setProduct_type(1);
        voucher.set_check_date(false);
        m.setVoucher(voucher);
        //attibutes
        ArrayList<SendoCreateOrUpdateProductPayload.Attribute> attributeList = new ArrayList<>();
        ArrayList<SendoCreateOrUpdateProductPayload.Variant> variantList = new ArrayList<>();


        //xu ly attribute list

        for (var _opt : sysProductResponse.getOptions()) {
            SendoCreateOrUpdateProductPayload.Attribute _attribute = new SendoCreateOrUpdateProductPayload.Attribute();
            _attribute.setAttribute_id(Integer.parseInt("4012" + ZeusUtils.paddingId(_opt.getId().toString()))); //prefix 88 với các attribute Id
            _attribute.setAttribute_name(_opt.getName());
            _attribute.setAttribute_code("4012" + ZeusUtils.paddingId(_opt.getId().toString()));
            _attribute.setAttribute_is_custom(true);
            _attribute.setAttribute_is_checkout(true);
            _attribute.setAttribute_is_required(false);

            ArrayList<SendoCreateOrUpdateProductPayload.AttributeValue> listAttributeValue = new ArrayList<>();
            for (var _optValue : _opt.getValues()) {
                SendoCreateOrUpdateProductPayload.AttributeValue _attributeVal = new SendoCreateOrUpdateProductPayload.AttributeValue();
                _attributeVal.setId(Integer.parseInt("3912" + ZeusUtils.paddingId(_optValue.getId().toString()))); //prefix 66 với các optionId
                _attributeVal.setValue(_optValue.getName());
                _attributeVal.set_custom(true);
                _attributeVal.set_selected(true);
//
//                _attributeVal.setAttribute_img("");
                listAttributeValue.add(_attributeVal);
            }

            _attribute.setAttribute_values(listAttributeValue);
            attributeList.add(_attribute);
        }
        m.setAttributes(attributeList);
        //xong attributes


        for (var _sysVariant : sysProductResponse.getVariants()) {
            SendoCreateOrUpdateProductPayload.Variant _variant = new SendoCreateOrUpdateProductPayload.Variant();
            _variant.setVariant_price((new BigDecimal(_sysVariant.getCost()).intValue()));
            _variant.setVariant_sku(_sysVariant.getSku());
            //xu ly variant_attributes
            ArrayList<SendoCreateOrUpdateProductPayload.VariantAttribute> listVariantAttributeInVariant = new ArrayList<>();

            for (var _optionVal : _sysVariant.getOptionValues()) {

                SendoCreateOrUpdateProductPayload.VariantAttribute __variantAttribute = new SendoCreateOrUpdateProductPayload.VariantAttribute();
                __variantAttribute.setAttribute_id(Integer.parseInt("4012" + ZeusUtils.paddingId(_optionVal.getIdOptionModel().toString())));
                __variantAttribute.setAttribute_code("4012" + ZeusUtils.paddingId(_optionVal.getIdOptionModel().toString()));
                __variantAttribute.setOption_id(Integer.parseInt("3912" + ZeusUtils.paddingId(_optionVal.getId().toString())));
                listVariantAttributeInVariant.add(__variantAttribute);
            }

            _variant.setVariant_attributes(listVariantAttributeInVariant);

            //xu ly stock
            ProductResponse.VariantResponse.StockResponse stockResponseWarehouse = _sysVariant.getStockInWarehouse(warehouseLinked.getId());
            if (stockResponseWarehouse == null) {
                _variant.setVariant_quantity(0);
            } else {
                _variant.setVariant_quantity(stockResponseWarehouse.getQuantity());
            }

            variantList.add(_variant);
        }
        m.setVariants(variantList);
        //chuyen phat
        SendoCreateOrUpdateProductPayload.ExtendedShippingPackage extendedShippingPackage = new SendoCreateOrUpdateProductPayload.ExtendedShippingPackage();
        extendedShippingPackage.set_using_standard(true);

        m.setExtended_shipping_package(extendedShippingPackage);
        return m;
    }

    @Override
    public SendoCreateOrUpdateProductPayload postNewProductToSendo(Long ecomId, Long sysProductId) {
        SendoLinkAccountRequestDTOWithModel sendoInfo = ecomService.getSendoDTO(ecomId);

        var response = sendoProductService.pulishCreateSystemProductToSendo(ecomId, sysProductId);

        KafkaPublishProductSendoPayload kafkaPublishProductSendoPayload = new KafkaPublishProductSendoPayload();
        kafkaPublishProductSendoPayload.setShop_key(sendoInfo.getShop_key());
        kafkaPublishProductSendoPayload.setSecret_key(sendoInfo.getSecret_key());
        kafkaPublishProductSendoPayload.setPublish_data_json(response);
        var payloadStr = TaurusBus.buildPayloadMessageString(kafkaPublishProductSendoPayload, "sendo.product.publish");
        //Page<ProductResponse> responses = products.map(product -> new ProductResponse(product, warehouseIds));
        kafkaTemplate.send("publish_sendo_product", payloadStr);
        return response;
    }

    @Override
    public boolean postAllProductToSendo(Long ecomId) {
        EcomAccount ecomAccount = ecomAccountRepository.getEcomAccountById(ecomId);
        if (ecomAccount == null) {
            logger.info("[-] Ecom Account Id {} not found", ecomId);
            throw new ZeusGlobalException(HttpStatus.OK, "Tài khoản sàn không tồn tại");
        }

        if (ecomAccount.getEcomName() != EcomAccount.EcomName.SENDO) {
            throw new ZeusGlobalException(HttpStatus.OK, "Không phải sàn SENDO");
        }
        User gm = ecomAccount.getGeneralManager();


        // Lấy liên kết sww and ecom account
        Optional<LazadaSwwAndEcomAccount> linkSwwOpt = lazadaSwwAndEcomAccountRepository.findByEcomAccountId(ecomAccount.getId());
        if (linkSwwOpt.isEmpty()) {
            logger.info("[-] Chua lien ket ecom id {}, gm {}. ", ecomId, gm.getId());
            throw new ZeusGlobalException(HttpStatus.OK, "Không tìm thấy liên kết với tài khoản sàn");
        }
        LazadaSwwAndEcomAccount linkSww = linkSwwOpt.get();
        Warehouse warehouseLinked = linkSww.getSaleChannelShop().getWarehouse();

        List<Product> listProducts = productRepository.findAll(
                ProductSpecs.hasGeneralManager(gm.getId())
        );
        for (var p:
                listProducts) {
            this.postNewProductToSendo(ecomId,p.getId());
        }

        return true;
    }

    @Override
    public boolean fetchAndSyncSendoProducts(Long ecomId) {
        EcomAccount ecomAccount = ecomAccountRepository.getEcomAccountById(ecomId);
        if (ecomAccount == null) {
            throw new ZeusGlobalException(HttpStatus.OK, "Không tồn tại tài khoản EcomId");
        }
        if (ecomAccount.getEcomName() != EcomAccount.EcomName.SENDO) {
            throw new ZeusGlobalException(HttpStatus.OK, "Không phải sàn SENDO");
        }

        User user = ecomAccount.getGeneralManager();

        String shopKey = ecomAccount.getAccountName();
        String secretKey = ecomAccount.parseSendoSellerInfoPayload().getData().getSecret_key();
        var reqPayload = SendoLinkAccountRequestDTO.builder().secret_key(secretKey).shop_key(shopKey).build();
        HashMap<String, String> headerAuth = new HashMap<String, String>();
        headerAuth.put("shop_key", shopKey);
        headerAuth.put("secret_key", secretKey);
        var responseModel = (new SendoServiceClient(appProperties.getServiceGoSendo()).Post("/sendo/product/crawlAllProductToDB", headerAuth, null, SendoServiceResponseBase.class));
        if (responseModel.success == true) {
            return true;
        }
        return false;
    }

}
