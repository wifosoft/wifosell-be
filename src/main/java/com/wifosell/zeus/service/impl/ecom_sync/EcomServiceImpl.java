package com.wifosell.zeus.service.impl.ecom_sync;


import com.google.gson.Gson;
import com.lazada.lazop.api.LazopClient;
import com.lazada.lazop.api.LazopRequest;
import com.lazada.lazop.util.ApiException;
import com.wifosell.zeus.config.property.AppProperties;
import com.wifosell.zeus.exception.ZeusGlobalException;
import com.wifosell.zeus.model.category.Category;
import com.wifosell.zeus.model.ecom_sync.*;
import com.wifosell.zeus.model.product.Product;
import com.wifosell.zeus.model.product.Variant;
import com.wifosell.zeus.model.sale_channel.SaleChannel;
import com.wifosell.zeus.model.shop.SaleChannelShop;
import com.wifosell.zeus.model.shop.Shop;
import com.wifosell.zeus.model.stock.Stock;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.model.warehouse.Warehouse;
import com.wifosell.zeus.payload.provider.ecom.AccountInfoPayload;
import com.wifosell.zeus.payload.provider.ecom.AccountInfoPayloadData;
import com.wifosell.zeus.payload.provider.lazada.ResponseCategoryAttributePayload;
import com.wifosell.zeus.payload.provider.lazada.ResponseCategoryTreePayload;
import com.wifosell.zeus.payload.provider.lazada.ResponseListProductPayload;
import com.wifosell.zeus.payload.provider.lazada.ResponseSellerInfoPayload;
import com.wifosell.zeus.payload.provider.lazada.report.GetAllProductReport;
import com.wifosell.zeus.payload.provider.lazada.report.GetProductPageReport;
import com.wifosell.zeus.payload.provider.shopee.ResponseLinkAccountPayload;
import com.wifosell.zeus.payload.request.ecom_sync.EcomAccountLazadaCallbackPayload;
import com.wifosell.zeus.payload.request.ecom_sync.SendoLinkAccountRequestDTO;
import com.wifosell.zeus.repository.*;
import com.wifosell.zeus.repository.ecom_sync.*;
import com.wifosell.zeus.service.EcomService;
import com.wifosell.zeus.specs.CategorySpecs;
import com.wifosell.zeus.specs.EcomAccountSpecs;
import com.wifosell.zeus.specs.LazadaCategorySpecs;
import com.wifosell.zeus.specs.VariantSpecs;
import com.wifosell.zeus.taurus.lazada.LazadaClient;
import com.wifosell.zeus.taurus.sendo.SendoServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service()
public class EcomServiceImpl implements EcomService {
    Logger logger = LoggerFactory.getLogger(EcomServiceImpl.class);


    @Autowired
    AppProperties appProperties;

    @Autowired
    ProductRepository productRepository;
    @Autowired
    StockRepository stockRepository;
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    VariantRepository variantRepository;

    @Autowired
    EcomAccountRepository ecomAccountRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    LazadaVariantRepository lazadaVariantRepository;
    @Autowired
    LazadaProductRepository lazadaProductRepository;
    @Autowired
    LazadaCategoryAttributeRepository lazadaCategoryAttributeRepository;
    @Autowired
    LazadaCategoryRepository lazadaCategoryRepository;

    @Autowired
    LazadaCategoryAndAttributeRepository lazadaCategoryAndAttributeRepository;


    @Autowired
    LazadaSwwAndEcomAccountRepository lazadaSwwAndEcomAccountRepository;

    @Autowired
    SaleChannelRepository saleChannelRepository;

    @Autowired
    WarehouseRepository warehouseRepository;
    @Autowired
    ShopRepository shopRepository;

    @Autowired
    SaleChannelShopRepository saleChannelShopRepository;

    //link lazada varirant, cate

    @Autowired
    LazadaCategoryAndSysCategoryRepository lazadaCategoryAndSysCategoryRepository;

    @Autowired
    LazadaVariantAndSysVarirantRepository lazadaVariantAndSysVarirantRepository;


    @Override
    public List<EcomAccount> getListEcomAccount(Long userId) {
        Long gmId = userId == null ? null : userRepository.getUserById(userId).getGeneralManager().getId();
        return ecomAccountRepository.findAll(
                EcomAccountSpecs.hasGeneralManager(gmId)
        );
    }


    public boolean deleteEcomAccount(Long ecomAccountId) {
        if (!ecomAccountRepository.existsById(ecomAccountId)) {
            throw new ZeusGlobalException(HttpStatus.OK, "ID không tồn tại");
        }
        ecomAccountRepository.deleteById(ecomAccountId);
        return true;
    }

    @Override
    public GetAllProductReport getAllProductsFromEcommerce(Long ecomId, int limitPerPage) {
        GetAllProductReport getAllProductReport = GetAllProductReport.builder()
                .totalProduct(0)
                .totalSku(0).build();
        int offset = 0;
        int totalProduct = 0;
        int totalSku = 0;
        while (true) {
            try {
                GetProductPageReport getProductPageReport = this.getProductsFromEcommerce(ecomId, offset, limitPerPage);
                totalProduct += getProductPageReport.getTotalProduct();
                totalSku += getProductPageReport.getTotalSku();
                this.logger.info("getAllProductsFromEcommerce() PROCCESSED offset: {} and limit: {}", offset, limitPerPage);
                offset += limitPerPage;


                if (getProductPageReport.getResponseListProductPayload().getData().getTotal_products() <= offset) {
                    this.logger.info("getAllProductsFromEcommerce() BREAK empty products offset: {} and limit: {}", offset, limitPerPage);
                    break;
                }

            } catch (Exception ex) {
                this.logger.error("getAllProductsFromEcommerce() Fail at offset: {} and limit: {}" + ex.getMessage(), offset, limitPerPage);
                break;
            }
        }
        getAllProductReport.setTotalProduct(totalProduct);
        getAllProductReport.setTotalSku(totalSku);
        return getAllProductReport;
    }

    @Override
    public GetProductPageReport getProductsFromEcommerce(Long ecomId, int offset, int limit) throws ApiException {
        boolean flagModeCreateSysProduct = true;
        Gson gson = (new Gson());
        EcomAccount ecomAccount = ecomAccountRepository.getEcomAccountById(ecomId);

        LazadaSwwAndEcomAccount lazadaSwwAndEcomAccount = lazadaSwwAndEcomAccountRepository.findByEcomAccountId(ecomId).orElseThrow(
                () -> new ZeusGlobalException(HttpStatus.OK, "Tài khoản chưa liên kết")
        );

        Warehouse warehouse = lazadaSwwAndEcomAccount.getSaleChannelShop().getWarehouse();

        String token = ecomAccount.getAccessToken();
        int totalProduct = 0;
        AtomicInteger totalSku = new AtomicInteger();

        LazopRequest request = new LazopRequest();
        request.setApiName("/products/get");
        request.setHttpMethod("GET");
        request.addApiParameter("filter", "live");
        request.addApiParameter("update_before", "");
        request.addApiParameter("create_before", "");
        request.addApiParameter("offset", String.valueOf(offset));
        request.addApiParameter("create_after", "");
        request.addApiParameter("update_after", "");
        request.addApiParameter("limit", String.valueOf(limit));
        request.addApiParameter("options", "1");
        request.addApiParameter("sku_seller_list", "");

        ResponseListProductPayload responseListProductPayload = LazadaClient.executeMappingModel(request, ResponseListProductPayload.class, token);
        ResponseListProductPayload.Data responseListProductData = responseListProductPayload.getData();
        List<ResponseListProductPayload.Product> listLazadaProducts = responseListProductData.products;

        totalProduct = listLazadaProducts.size();
        for (ResponseListProductPayload.Product e : listLazadaProducts) {
            logger.info("Process product  {}", e.getAttributes().getName());
            LazadaCategory lazadaCategory = lazadaCategoryRepository.findFirstByLazadaCategoryId(e.getPrimary_category()).orElse(null);
            if (lazadaCategory == null) {
                logger.info("Lazada category null {}", e.getPrimary_category());
                continue;
            }
            Optional<LazadaCategoryAndSysCategory> lazadaCategoryAndSysCategory = lazadaCategoryAndSysCategoryRepository.findByLazadaCategory(lazadaCategory.getId());
            Category sysCategory = null;
            if (lazadaCategoryAndSysCategory.isPresent()) {
                sysCategory = lazadaCategoryAndSysCategory.get().getSysCategory();
            } else {
                sysCategory = null;
            }
            logger.info("[+] Processed product item_id : {} - Name: {}", e.getItem_id(), e.getAttributes().getName());
            //kiem tra lzproduct ton tai khong
            LazadaProduct lzProduct = lazadaProductRepository.findByItemId(e.getItem_id());
            if (lzProduct == null) {
                lzProduct = new LazadaProduct(e, ecomAccount);
            } else {
                lzProduct.withDataByProductAPI(e).setEcomAccount(ecomAccount);
            }
            logger.info("Save product {}", lzProduct.getName());
            lazadaProductRepository.save(lzProduct);

            //xu ly sku
            List<ResponseListProductPayload.Sku> listSkus = e.getSkus();
            LazadaProduct finalLzProduct = lzProduct;

            boolean flagCacheCreatedProduct = false;
            Product sysProduct = null;
            for (ResponseListProductPayload.Sku s : listSkus) {
                LazadaVariant lzVariant = lazadaVariantRepository.findBySkuId(s.getSkuId());
                if (lzVariant == null) {
                    //tao moi variant link voi lazadaProduct
                    lzVariant = new LazadaVariant(s, finalLzProduct);
                } else {
                    lzVariant.withDataBySkuAPI(s);
                }
                lazadaVariantRepository.save(lzVariant);


                Variant sysVariant = variantRepository.getBySKUNoThrow(lzVariant.getSellerSku(), ecomAccount.getGeneralManager().getId());
                if (sysVariant != null) {
                    //nếu tồn tại variant thì sẽ tồn tại product => Cập nhật thông tin product
                    //cập nhật thông tin variant hiện tại, stock ở warehouse tương ứng
                    sysVariant.setOriginalCost(new BigDecimal(lzVariant.getPrice()));
                    sysVariant.setCost(new BigDecimal(lzVariant.getPrice()));
                    variantRepository.save(sysVariant);
                    Optional<Stock> stock_ = stockRepository.findByVariantAndWarehouse(sysVariant.getId(), warehouse.getId());
                    if (stock_.isPresent()) {
                        Stock stock = stock_.get();
                        stock.setQuantity(s.getQuantity());
                        stock.setActualQuantity(s.getSellableStock());
                        stockRepository.save(stock);
                    } else {
                        //chưa tồn tại stock warehouse thì thêm record

                        Stock stock = new Stock();
                        stock.setActualQuantity(s.getSellableStock());
                        stock.setQuantity(s.getQuantity());
                        stock.setVariant(sysVariant);
                        stock.setWarehouse(warehouse);
                        stockRepository.save(stock);
                    }
                } else {
                    //Không tồn tại variant thì có thể có tồn tại product hoặc không
                    //Thêm product + variant
                    if (!flagCacheCreatedProduct) {
                        sysProduct = new Product();
                        sysProduct.setProductIdentify(s.getSellerSku());
                        sysProduct.setDescription(e.getAttributes().getDescription());
                        sysProduct.setLength(new BigDecimal(s.getPackage_length()));
                        sysProduct.setWidth(new BigDecimal(s.getPackage_width()));
                        sysProduct.setHeight(new BigDecimal(s.getPackage_height()));
                        sysProduct.setName(e.getAttributes().getName());
                        sysProduct.setWeight(new BigDecimal(s.getPackage_weight()));
                        sysProduct.setGeneralManager(ecomAccount.getGeneralManager());
                        sysProduct.setCategory(sysCategory);
                        productRepository.save(sysProduct);
                        flagCacheCreatedProduct = true;
                    }
                    Variant variant = new Variant();
                    variant.setOriginalCost(new BigDecimal(lzVariant.getPrice()));
                    variant.setCost(new BigDecimal(lzVariant.getPrice()));
                    variant.setBarcode(s.getSellerSku());
                    variant.setSku(s.getSellerSku());
                    variant.setGeneralManager(ecomAccount.getGeneralManager());
                    variant.setProduct(sysProduct);
                    variantRepository.save(variant);
                    Stock stock = new Stock();
                    stock.setActualQuantity(s.getSellableStock());
                    stock.setQuantity(s.getQuantity());
                    stock.setVariant(variant);
                    stock.setWarehouse(warehouse);
                    stockRepository.save(stock);
                }
                totalSku.addAndGet(1);
            }
        }

        GetProductPageReport getProductPageReport = new GetProductPageReport(totalProduct, totalSku.intValue());
        getProductPageReport.setResponseListProductPayload(responseListProductPayload);

        return getProductPageReport;
    }


    @Override
    public EcomAccount addEcomAccountLazada(Long userId, EcomAccount account) {
        User user = userRepository.getUserById(userId);
        Optional<EcomAccount> checkExisted = ecomAccountRepository.findByAccountNameAndEcomName(account.getAccountName(), EcomAccount.EcomName.LAZADA);
        //update
        //throw new ZeusGlobalException(HttpStatus.OK, "Tài khoản đã tồn tại");
        checkExisted.ifPresent(ecomAccount -> {
                    account.setId(ecomAccount.getId());
                    account.setNote("Đã cập nhật token tài khoản đã tồn tại");
                }
        );
        account.setGeneralManager(user);
        ecomAccountRepository.save(account);
        return account;
    }


    public EcomAccount addEcomAccountLazadaFromCallback(EcomAccountLazadaCallbackPayload payloadCallback) throws ApiException {
        User user = userRepository.getUserById(payloadCallback.getUserId());

        EcomAccount ecomAccount = new EcomAccount();
        Optional.ofNullable(payloadCallback.getTokenAuthResponse().getAccount()).ifPresent(ecomAccount::setAccountName);
        Optional.ofNullable(payloadCallback).ifPresent(e -> {
            ecomAccount.setAuthResponse((new Gson()).toJson(payloadCallback));
        });
        Optional.ofNullable(payloadCallback.getTokenAuthResponse().getExpires_in()).ifPresent(e -> {
            LocalDateTime now = LocalDateTime.now();
            ecomAccount.setExpiredAt(now.plusSeconds(e));
        });
        ecomAccount.setDescription("Tài khoản lazada khởi tạo mới");
        ecomAccount.setNote("Đã đăng nhập thành công");
        ecomAccount.setIsActive(true);
        ecomAccount.setAccountStatus(EcomAccount.AccountStatus.AUTH);
        ecomAccount.setEcomName(EcomAccount.EcomName.LAZADA);

        LazopRequest request = new LazopRequest();
        request.setApiName("/seller/get");
        request.setHttpMethod("GET");
        ResponseSellerInfoPayload responseTokenPayload = LazadaClient.executeMappingModel(request, ResponseSellerInfoPayload.class, ecomAccount.getAccessToken());
        ecomAccount.setAccountInfo((new Gson()).toJson(responseTokenPayload));
        return this.addEcomAccountLazada(payloadCallback.getUserId(), ecomAccount);
    }

    @Override
    public EcomAccount addEcomAccountSendo(Long userId, String shopKey, String secretKey, String shopName) throws ApiException {
        User user = userRepository.getUserById(userId);
        EcomAccount ecomAccount;

        //request to service sendo
        var reqPayload = SendoLinkAccountRequestDTO
                .builder()
                .secret_key(secretKey)
                .shop_key(shopKey).build();
        var responseModel = (new SendoServiceClient(appProperties.getServiceGoSendo()).Post("/sendo/account/login", reqPayload, ResponseLinkAccountPayload.class));
        if (responseModel.success) {

            Gson gson = new Gson();
            var dataModel = responseModel.getData();

            AccountInfoPayload accountInfoPayload = AccountInfoPayload.builder()
                    .code("0")
                    .request_id("2101241b16547560579496455").data(
                            AccountInfoPayloadData.builder()
                                    .name(shopName)
                                    .verified("true")
                                    .location("")
                                    .seller_id(dataModel.get_id())
                                    .status("ACTIVE")
                                    .email("")
                                    .secret_key(dataModel.getSecret_key())
                                    .shop_key(dataModel.getShop_key())
                                    .build()
                    ).build();

            //check existed
            Optional<EcomAccount> checkExisted = ecomAccountRepository.findByAccountNameAndEcomName(shopKey, EcomAccount.EcomName.SENDO);

            if (checkExisted.isEmpty()) {
                ecomAccount = new EcomAccount();
                ecomAccount.setDescription("Tài khoản sendo khởi tạo mới");
                ecomAccount.setGeneralManager(user);
            } else {
                ecomAccount = checkExisted.get();
                ecomAccount.setDescription("Cập nhật tài khoản sendo");
            }

            Optional.ofNullable(dataModel.getShop_key()).ifPresent(ecomAccount::setAccountName);
            Optional.ofNullable(responseModel).ifPresent(e -> {
                ecomAccount.setAuthResponse((new Gson()).toJson(responseModel));
            });

            Optional.ofNullable(dataModel.getExpires()).ifPresent(e -> {
                ecomAccount.setExpiredAt(LocalDateTime.ofInstant(e.toInstant(), ZoneId.systemDefault()));
            });
            ecomAccount.setNote("Đã đăng nhập thành công");
            ecomAccount.setIsActive(true);
            ecomAccount.setAccountStatus(EcomAccount.AccountStatus.AUTH);
            ecomAccount.setEcomName(EcomAccount.EcomName.SENDO);
            ecomAccount.setAccountInfo(gson.toJson(accountInfoPayload));
            ecomAccount.setGeneralManager(user);
            ecomAccountRepository.save(ecomAccount);

        } else {
            throw new ApiException("Đăng nhập không thành công. Vui lòng liên hệ quản trị viên");
        }

        return ecomAccount;
    }

    public EcomAccount getEcomAccount(Long id) {
        return ecomAccountRepository.getEcomAccountById(id);
    }


    public void saveLazadaCategory(ResponseCategoryTreePayload.CategoryTreeItem categoryTreeItem, LazadaCategory parent) {
        LazadaCategory lazadaCategory = new LazadaCategory(categoryTreeItem, parent);
        lazadaCategoryRepository.save(lazadaCategory);
        if (categoryTreeItem.getChildren() != null) {
            for (ResponseCategoryTreePayload.CategoryTreeItem item : categoryTreeItem.getChildren()) {
                saveLazadaCategory(item, lazadaCategory);
            }

        }
    }


    @Transactional
    public void crawlSingleCategoryAttributeById(Long lazadaCategoryId) throws ApiException {
        LazadaCategory lazadaCategory = lazadaCategoryRepository.findFirstByLazadaCategoryId(lazadaCategoryId).orElseThrow(() -> new ZeusGlobalException(HttpStatus.OK, "Không tồn tại category id"));

        LazopRequest request = new LazopRequest();
        request.setApiName("/category/attributes/get");
        request.setHttpMethod("GET");
        request.addApiParameter("primary_category_id", lazadaCategoryId.toString());
        request.addApiParameter("language_code", "vi_VN");
        ResponseCategoryAttributePayload responseTokenPayload = LazadaClient.executeMappingModel(request, ResponseCategoryAttributePayload.class);
        LazadaCategoryAttribute lazadaCategoryAttribute = null;
        for (ResponseCategoryAttributePayload.CategoryAttributeItem categoryAttributeItem : responseTokenPayload.getData()) {
            try {
                // kiem tra co ton tai attribute lazada id nay khong
                Optional<LazadaCategoryAttribute> lazadaCategoryAttributeOpt = lazadaCategoryAttributeRepository.findFirstByLazadaAttributeId(categoryAttributeItem.getId());
                if (lazadaCategoryAttributeOpt.isEmpty()) {
                    //khong ton tai thi them vao db
                    lazadaCategoryAttribute = new LazadaCategoryAttribute(categoryAttributeItem);
                    lazadaCategoryAttributeRepository.save(lazadaCategoryAttribute);
                } else {
                    lazadaCategoryAttribute = lazadaCategoryAttributeOpt.get();
                }
                LazadaCategoryAndAttribute lazadaCategoryAndAttribute = new LazadaCategoryAndAttribute();
                lazadaCategoryAndAttribute.setLazadaCategoryAttribute(lazadaCategoryAttribute);
                lazadaCategoryAndAttribute.setLazadaCategory(lazadaCategory);
                lazadaCategoryAndAttributeRepository.save(lazadaCategoryAndAttribute);
            } catch (Exception exception) {
                logger.info("[+] Error when process categoryId {}", lazadaCategoryId);
                exception.printStackTrace();
            }
        }

        String responseJson = (new Gson()).toJson(responseTokenPayload);
        System.out.println(responseJson);
    }

    @Override
    public void crawlCategoryAttribute() throws ApiException {
        //crawl by db
        List<LazadaCategory> listLeafCategories = lazadaCategoryRepository.findAllLeaf();
        logger.info("Crawl Category Attribute {} items", listLeafCategories.size());
        for (LazadaCategory lazadaCategory : listLeafCategories) {
            //crawl the option by API
            try {
                crawlSingleCategoryAttributeById(lazadaCategory.getLazadaCategoryId());
            } catch (Exception ex) {
                logger.info("[+] Error when process categoryId {}", lazadaCategory.getLazadaCategoryId());
                ex.printStackTrace();
            }
        }
        logger.info("Crawl completed Category Attribute {} items", listLeafCategories.size());

    }

    public void crawlCategoryTree() throws ApiException {
        LazopClient lazadaClient = LazadaClient.getClient();

        LazopRequest request = new LazopRequest();
        request.setApiName("/category/tree/get");
        request.setHttpMethod("GET");
        request.addApiParameter("language_code", "vi_VN");
        ResponseCategoryTreePayload responseTokenPayload = LazadaClient.executeMappingModel(request, ResponseCategoryTreePayload.class);

        List<ResponseCategoryTreePayload.CategoryTreeItem> categoryTreeItemList = responseTokenPayload.getData();
        for (ResponseCategoryTreePayload.CategoryTreeItem item : categoryTreeItemList) {
            saveLazadaCategory(item, null);
        }
        String responseJson = (new Gson()).toJson(responseTokenPayload);
        System.out.println(responseJson);
    }

    public List<LazadaCategoryAttribute> getListCategoryAttribute() {
        List<LazadaCategoryAttribute> listCategoryAttribute = lazadaCategoryAttributeRepository.findAll();
        return listCategoryAttribute;
    }

    public LazadaSwwAndEcomAccount linkEcomAccountToSSW(Long ecomId, Long sswId) {
        EcomAccount ecomAccount = ecomAccountRepository.findById(ecomId).orElseThrow(
                () -> new ZeusGlobalException(HttpStatus.OK, "Ecom account không tồn tại")
        );
        SaleChannelShop ssw = saleChannelShopRepository.findById(sswId).orElseThrow(
                () -> new ZeusGlobalException(HttpStatus.OK, "Salechannel_shop_warehouse không tồn tại")
        );
        LazadaSwwAndEcomAccount lazadaSwwAndEcomAccount = new LazadaSwwAndEcomAccount();
        lazadaSwwAndEcomAccount.setEcomAccount(ecomAccount);
        lazadaSwwAndEcomAccount.setSaleChannelShop(ssw);
        lazadaSwwAndEcomAccountRepository.save(lazadaSwwAndEcomAccount);
        return lazadaSwwAndEcomAccount;
    }


    public LazadaSwwAndEcomAccount linkEcomAccountToSSW(Long ecomId, Long saleChannelId, Long shopId, Long warehouseId) {
        EcomAccount ecomAccount = ecomAccountRepository.findById(ecomId).orElse(null);
        SaleChannel saleChannel = saleChannelRepository.findById(saleChannelId).orElseThrow(
                () -> new ZeusGlobalException(HttpStatus.OK, "saleChannelId không tồn tại")
        );

        Shop shop = shopRepository.findById(shopId).orElseThrow(
                () -> new ZeusGlobalException(HttpStatus.OK, "shopId không tồn tại")
        );

        Warehouse warehouse = warehouseRepository.findById(warehouseId).orElseThrow(
                () -> new ZeusGlobalException(HttpStatus.OK, "warehouseId không tồn tại")
        );

        Optional<SaleChannelShop> ssw = saleChannelShopRepository.findRecordBySSWId(shopId, saleChannelId, warehouseId);
        SaleChannelShop sswRecord = null;
        if (ssw.isPresent()) {
            //ton tai
            sswRecord = ssw.get();
        } else {
            sswRecord = SaleChannelShop.builder().saleChannel(saleChannel).shop(shop).warehouse(warehouse).build();
            saleChannelShopRepository.save(sswRecord);
        }

        //kiem tra ton tai link khong thi link
        if (ecomAccount == null) {
            return LazadaSwwAndEcomAccount.builder().saleChannelShop(sswRecord).ecomAccount(null).build();
        }
        Optional<LazadaSwwAndEcomAccount> linkSwwAndEcomAccount = lazadaSwwAndEcomAccountRepository.getRecordBySswIdAndEcomAccountId(sswRecord.getId(), ecomId);
        LazadaSwwAndEcomAccount linkwwAndEcomAccountRecord = null;
        if (linkSwwAndEcomAccount.isPresent()) {
            //ton tai
            linkwwAndEcomAccountRecord = linkSwwAndEcomAccount.get();
        } else {
            linkwwAndEcomAccountRecord = LazadaSwwAndEcomAccount.builder().ecomAccount(ecomAccount).saleChannelShop(sswRecord).build();
            lazadaSwwAndEcomAccountRepository.save(linkwwAndEcomAccountRecord);
        }
        return linkwwAndEcomAccountRecord;
    }


    public LazadaCategoryAndSysCategory linkLazadaCategoryAndSysCategory(User user, Long lazadaCategoryId, Long sysCategoryId) {
        LazadaCategoryAndSysCategory reslt = null;

        User gm = user.getGeneralManager();

        Category sysCategory = categoryRepository.getOne(
                CategorySpecs.hasGeneralManager(gm.getId()).and(CategorySpecs.hasId(sysCategoryId)));

        LazadaCategory lazadaCategory = lazadaCategoryRepository.findById(lazadaCategoryId).orElseThrow(
                () -> new ZeusGlobalException(HttpStatus.OK, "Không tồn tại lazada category")
        );
        if (!lazadaCategory.isLeaf()) {
            throw new ZeusGlobalException(HttpStatus.OK, "Lazada category cần là category cấp cuối cùng.");
        }

        reslt = new LazadaCategoryAndSysCategory();
        reslt.setLazadaCategory(lazadaCategory);
        reslt.setSysCategory(sysCategory);
        reslt.setGeneralManager(gm.getGeneralManager());
        lazadaCategoryAndSysCategoryRepository.save(reslt);
        return reslt;
    }


    //lấy danh sách category đã linked
    public List<LazadaCategoryAndSysCategory> getLinkedLazadaCategoryAndSysCategory(User user) {
        List<LazadaCategoryAndSysCategory> listLazadaCategoryAndSystemCategory = new ArrayList<>();
        User gm = user.getGeneralManager();
        listLazadaCategoryAndSystemCategory = lazadaCategoryAndSysCategoryRepository.findAllByGeneralManager(gm.getId());
        return listLazadaCategoryAndSystemCategory;
    }

    public LazadaVariantAndSysVariant linkLazadaVariantAndSysVariant(User user, Long lazadaVariantId, Long sysVariantId) {
        LazadaVariantAndSysVariant record = null;
        User gm = user.getGeneralManager();

        LazadaVariant lazadaVariant = lazadaVariantRepository.findById(lazadaVariantId).orElseThrow(
                () -> new ZeusGlobalException(HttpStatus.OK, "Không tồn tại lazadaVariantId")
        );

        Variant sysVariant = variantRepository.getOne(
                VariantSpecs.hasGeneralManager(gm.getId()).and(VariantSpecs.hasId(sysVariantId))
        );

        record = new LazadaVariantAndSysVariant();
        record.setLazadaVariant(lazadaVariant);
        record.setVariant(sysVariant);
        record.setGeneralManager(gm);
        lazadaVariantAndSysVarirantRepository.save(record);
        return record;
    }

    @Override
    public List<LazadaCategory> getListCategory(boolean isLeaf) {
        return lazadaCategoryRepository.findAll(
                LazadaCategorySpecs.isLeaf(isLeaf)
        );
    }


}

