package com.wifosell.zeus.service.impl.ecom_sync;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lazada.lazop.util.ApiException;
import com.wifosell.lazada.modules.category.LazadaCategoryAPI;
import com.wifosell.lazada.modules.category.payload.LazadaGetCategoryAttributesResponse;
import com.wifosell.lazada.modules.image.LazadaImageAPI;
import com.wifosell.lazada.modules.image.payload.LazadaMigrateImagesBatchResponse;
import com.wifosell.lazada.modules.image.payload.LazadaMigrateImagesRequest;
import com.wifosell.lazada.modules.image.payload.LazadaMigrateImagesResponse;
import com.wifosell.lazada.modules.product.LazadaProductAPI;
import com.wifosell.lazada.modules.product.payload.*;
import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.exception.AppException;
import com.wifosell.zeus.model.attribute.Attribute;
import com.wifosell.zeus.model.category.Category;
import com.wifosell.zeus.model.ecom_sync.*;
import com.wifosell.zeus.model.option.OptionModel;
import com.wifosell.zeus.model.option.OptionValue;
import com.wifosell.zeus.model.product.Product;
import com.wifosell.zeus.model.product.ProductImage;
import com.wifosell.zeus.model.product.Variant;
import com.wifosell.zeus.model.product.VariantValue;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.model.warehouse.Warehouse;
import com.wifosell.zeus.payload.GApiErrorBody;
import com.wifosell.zeus.payload.provider.lazada.report.FetchLazadaProductsReport;
import com.wifosell.zeus.payload.provider.lazada.report.PushLazadaProductsReport;
import com.wifosell.zeus.payload.request.product.IProductRequest;
import com.wifosell.zeus.payload.request.product.UpdateProductRequest;
import com.wifosell.zeus.repository.OptionRepository;
import com.wifosell.zeus.repository.ProductRepository;
import com.wifosell.zeus.repository.UserRepository;
import com.wifosell.zeus.repository.ecom_sync.*;
import com.wifosell.zeus.service.*;
import com.wifosell.zeus.specs.LazadaProductSpecs;
import com.wifosell.zeus.specs.LazadaVariantSpecs;
import com.wifosell.zeus.utils.ZeusUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service("LazadaProductService")
@Transactional
@RequiredArgsConstructor
public class LazadaProductServiceImpl implements LazadaProductService {
    private static final Logger logger = LoggerFactory.getLogger(LazadaProductServiceImpl.class);

    private final UserRepository userRepository;

    private final EcomService ecomService;
    private final LazadaSwwAndEcomAccountRepository lazadaSwwAndEcomAccountRepository;

    private final LazadaCategoryRepository lazadaCategoryRepository;
    private final LazadaProductRepository lazadaProductRepository;
    private final LazadaVariantRepository lazadaVariantRepository;

    private final LazadaCategoryAndSysCategoryRepository lazadaCategoryAndSysCategoryRepository;
    private final LazadaProductAndSysProductRepository lazadaProductAndSysProductRepository;
    private final LazadaVariantAndSysVariantRepository lazadaVariantAndSysVariantRepository;

    private final ProductService productService;
    private final ProductRepository productRepository;
    private final VariantService variantService;
    private final OptionRepository optionRepository;
    private final StockService stockService;

    @Override
    public FetchLazadaProductsReport fetchLazadaProducts(Long userId, Long ecomId) {
        User user = userRepository.getUserById(userId);
        EcomAccount ecomAccount = ecomService.getEcomAccount(userId, ecomId);

        LazadaSwwAndEcomAccount link = lazadaSwwAndEcomAccountRepository.getByEcomAccountId(ecomId);
        Warehouse warehouse = link.getSaleChannelShop().getWarehouse();

        int offset = 0;
        final int limit = 50;
        AtomicInteger fetchTotal = new AtomicInteger();
        AtomicInteger fetchSuccess = new AtomicInteger();

        while (true) {
            LazadaGetProductsResponse res;
            try {
                res = LazadaProductAPI.getProducts(ecomAccount.getAccessToken(), offset, limit);
            } catch (ApiException ignore) {
                throw new AppException(GApiErrorBody.makeErrorBody(EAppExceptionCode.LAZADA_API_ERROR, "Get products fail"));
            }

            if (res == null) {
                logger.error("getProducts fail | ecomId = {}", ecomAccount.getId());
                return null;
            }

            if (res.getData().getProducts() != null) {
                res.getData().getProducts().forEach(product -> {
                    try {
                        boolean success = fetchLazadaProductItem(user, ecomAccount, product.getItemId(), warehouse, null);
                        if (success) fetchSuccess.getAndIncrement();
                    } catch (ApiException e) {
                        e.printStackTrace();
                    } finally {
                        fetchTotal.getAndIncrement();
                    }
                });
                offset += res.getData().getProducts().size();
            }

            if (fetchTotal.get() >= res.getData().getTotalProducts())
                break;
        }

        logger.info("getProducts success | ecomId = {}, fetchTotal = {}, fetchSuccess = {}",
                ecomAccount.getId(), fetchTotal, fetchSuccess);
        return FetchLazadaProductsReport.builder().fetchTotal(fetchTotal.get()).fetchSuccess(fetchSuccess.get()).build();
    }

    private boolean fetchLazadaProductItem(User user, EcomAccount ecomAccount, Long itemId, Warehouse warehouse, Product syncProduct) throws ApiException {
        LazadaGetProductItemResponse res = LazadaProductAPI.getProductItem(ecomAccount.getAccessToken(), itemId);

        if (res == null) {
            logger.error("getProductItem fail | ecomId = {}, productId = {}", ecomAccount.getId(), itemId);
            return false;
        }

        LazadaGetProductItemResponse.Data data = res.getData();

        // Create/Update LazadaProduct
        LazadaCategory lazadaCategory = lazadaCategoryRepository.findByLazadaCategoryId(data.getPrimaryCategoryId())
                .orElse(null);

        if (lazadaCategory == null) {
            logger.error("getProductItem fail | Lazada category not found | ecomId = {}, itemId = {}, lazadaCategoryId = {}",
                    ecomAccount.getId(), itemId, data.getPrimaryCategoryId());
            return false;
        }

        LazadaProduct lazadaProduct = lazadaProductRepository.findByItemId(data.getItemId()).orElse(new LazadaProduct());
        lazadaProduct.injectData(data);
        lazadaProduct.setEcomAccount(ecomAccount);
        lazadaProduct.setGeneralManager(user.getGeneralManager());
        lazadaProductRepository.save(lazadaProduct);

        // Create/Update LazadaVariants
        List<LazadaVariant> lazadaVariants = new ArrayList<>();
        data.getSkus().forEach(sku -> {
            LazadaVariant lazadaVariant = lazadaVariantRepository.findBySkuId(sku.getSkuId()).orElse(new LazadaVariant());
            lazadaVariant.injectData(sku);
            lazadaVariant.setLazadaProduct(lazadaProduct);
            lazadaVariant.setEcomAccount(ecomAccount);
            lazadaVariant.setGeneralManager(user.getGeneralManager());
            lazadaVariantRepository.save(lazadaVariant);
            lazadaVariants.add(lazadaVariant);
        });

        // Create/Update SysProduct and SysVariants
        LazadaProductAndSysProduct productLink = lazadaProductAndSysProductRepository.findByLazadaProductId(lazadaProduct.getId()).orElse(null);
        Product sysProduct = syncProduct;

        if (sysProduct == null) {
            sysProduct = productLink != null ? productLink.getSysProduct() : new Product();
            sysProduct = productService.updateProductByRequest(
                    sysProduct,
                    toUpdateProductRequest(data, sysProduct),
                    user.getGeneralManager());

            LazadaCategoryAndSysCategory categoryLink = lazadaCategoryAndSysCategoryRepository.findByGeneralManagerIdAndLazadaCategoryId(
                    ecomAccount.getGeneralManager().getId(), lazadaCategory.getId()).orElse(null);
            Category sysCategory = categoryLink != null ? categoryLink.getSysCategory() : null;
            sysProduct.setCategory(sysCategory);

            productRepository.save(sysProduct);
        }

        // Link LazadaProduct and SysProduct
        if (productLink == null) {
            productLink = new LazadaProductAndSysProduct();
            productLink.setLazadaProduct(lazadaProduct);
            productLink.setSysProduct(sysProduct);
            productLink.setGeneralManager(user.getGeneralManager());
            lazadaProductAndSysProductRepository.save(productLink);
        }

        // Sort sysVariants by data.skus order
        List<Variant> sysVariants = new ArrayList<>();
        List<Variant> tempSysVariants = sysProduct.getVariants(true);
        data.getSkus().forEach(sku -> {
            for (Variant variant : tempSysVariants) {
                Map<String, String> skuOptions = new HashMap<>(sku.getOptions());
                for (VariantValue variantValue : variant.getVariantValues()) {
                    String optionName = variantValue.getOptionValue().getOption().getName();
                    String optionValue = variantValue.getOptionValue().getName();
                    if (optionValue.equals(skuOptions.get(optionName))) {
                        skuOptions.remove(optionName);
                    }
                }
                if (skuOptions.isEmpty()) {
                    sysVariants.add(variant);
                    break;
                }
            }
        });

        // Link LazadaVariants and SysVariants
        for (int i = 0; i < sysVariants.size(); ++i) {
            LazadaVariantAndSysVariant variantLink = lazadaVariantAndSysVariantRepository.findByLazadaVariantId(lazadaVariants.get(i).getId())
                    .orElse(new LazadaVariantAndSysVariant());
            variantLink.setSysVariant(sysVariants.get(i));
            variantLink.setLazadaVariant(lazadaVariants.get(i));
            variantLink.setGeneralManager(user.getGeneralManager());
            lazadaVariantAndSysVariantRepository.save(variantLink);
        }

        // Update stock
        for (int i = 0; i < sysVariants.size(); ++i) {
            stockService.updateStock(
                    warehouse,
                    sysVariants.get(i),
                    data.getSkus().get(i).getAvailable(),
                    data.getSkus().get(i).getAvailable());
        }

        logger.info("getProductItem success | ecomId = {}, itemId = {}, name = {}, skuCount = {}",
                ecomAccount.getId(), lazadaProduct.getItemId(), lazadaProduct.getName(), lazadaProduct.getSkuCount());
        return true;
    }

    private UpdateProductRequest toUpdateProductRequest(LazadaGetProductItemResponse.Data data, Product product) {
        UpdateProductRequest req = new UpdateProductRequest();

        // Metadata
        req.setName(data.getName());
        req.setDescription(data.getDescription());

        req.setWeight(Product.fromWeightLazada(data.getSkus().get(0).getPackageWeight()));
        req.setLength(data.getSkus().get(0).getPackageLength());
        req.setWidth(data.getSkus().get(0).getPackageWidth());
        req.setHeight(data.getSkus().get(0).getPackageHeight());

        req.setIsActive(data.isActive());

        // Images
        List<ProductImage> productImages = product.getImages(true);
        List<IProductRequest.ImageRequest> imageRequests = data.getImages().stream()
                .map(url -> {
                    Long imageId = null;
                    for (ProductImage productImage : productImages) {
                        if (productImage.getUrl().equals(url)) {
                            imageId = productImage.getId();
                            break;
                        }
                    }
                    return new IProductRequest.ImageRequest(imageId, url);
                })
                .collect(Collectors.toList());
        req.setImages(imageRequests);

        // Attributes
        List<Attribute> attributes = product.getAttributes(true);
        List<IProductRequest.AttributeRequest> attributeRequests = data.getAttributes().entrySet().stream()
                .map(entry -> {
                    Long attributeId = null;
                    for (Attribute attribute : attributes) {
                        if (attribute.getName().equals(entry.getKey()) && attribute.getValue().equals(entry.getValue())) {
                            attributeId = attribute.getId();
                            break;
                        }
                    }
                    IProductRequest.AttributeRequest attributeRequest = new IProductRequest.AttributeRequest();
                    attributeRequest.setId(attributeId);
                    attributeRequest.setName(entry.getKey());
                    attributeRequest.setValue(entry.getValue());
                    return attributeRequest;
                })
                .collect(Collectors.toList());
        req.setAttributes(attributeRequests);

        // Options
        List<IProductRequest.OptionRequest> optionRequests = new ArrayList<>();
        List<OptionModel> options = product.getOptions(true);

        data.getVariations().forEach(variation -> {
            IProductRequest.OptionRequest optionRequest = new IProductRequest.OptionRequest();

            Long optionId = null;
            OptionModel option = null;
            for (OptionModel o : options) {
                if (o.getName().equals(variation.getName())) {
                    option = o;
                    optionId = o.getId();
                    break;
                }
            }

            optionRequest.setId(optionId);
            optionRequest.setName(variation.getName());

            List<IProductRequest.OptionValueRequest> optionValueRequests = new ArrayList<>();
            List<OptionValue> optionValues = option != null ?
                    option.getOptionValues().stream().filter(ov -> !ov.isDeleted()).collect(Collectors.toList()) :
                    new ArrayList<>();

            variation.getOptions().forEach(value -> {
                IProductRequest.OptionValueRequest optionValueRequest = new IProductRequest.OptionValueRequest();
                Long optionValueId = null;
                for (OptionValue ov : optionValues) {
                    if (ov.getName().equals(value)) {
                        optionValueId = ov.getId();
                        break;
                    }
                }
                optionValueRequest.setId(optionValueId);
                optionValueRequest.setName(value);
                optionValueRequests.add(optionValueRequest);
            });

            optionRequest.setValues(optionValueRequests);

            optionRequests.add(optionRequest);
        });

        req.setOptions(optionRequests);

        // Variants
        List<IProductRequest.VariantRequest> variantRequests = new ArrayList<>();

        data.getSkus().forEach(sku -> {
            IProductRequest.VariantRequest variantRequest = new IProductRequest.VariantRequest();

            Long variantId = null;
            LazadaVariant lazadaVariant = lazadaVariantRepository.findBySkuId(sku.getSkuId()).orElse(null);
            if (lazadaVariant != null) {
                LazadaVariantAndSysVariant variantLink = lazadaVariantAndSysVariantRepository.findByLazadaVariantId(lazadaVariant.getId()).orElse(null);
                if (variantLink != null) {
                    variantId = variantLink.getSysVariant().getId();
                }
            }

            variantRequest.setId(variantId);
            variantRequest.setOriginalCost(sku.getPrice().toString());
            variantRequest.setCost(sku.getPrice().toString());
            variantRequest.setSku(sku.getSellerSku());
            variantRequest.setBarcode(null);
            variantRequest.setIsActive(sku.isActive());

            variantRequests.add(variantRequest);
        });

        req.setVariants(variantRequests);

        return req;
    }

    @Override
    public PushLazadaProductsReport pushLazadaProducts(Long userId, Long ecomId) {
        User user = userRepository.getUserById(userId);
        EcomAccount ecomAccount = ecomService.getEcomAccount(userId, ecomId);

        LazadaSwwAndEcomAccount ecomLink = lazadaSwwAndEcomAccountRepository.getByEcomAccountId(ecomId);
        Warehouse warehouse = ecomLink.getSaleChannelShop().getWarehouse();

        // Push products to Lazada
        List<Product> sysProducts = productService.getProducts(userId, List.of(warehouse.getId()), List.of(true));
        List<Long> itemIds = new ArrayList<>();
        List<Product> syncProducts = new ArrayList<>();
        int createTotal = 0;
        int createSuccess = 0;
        int updateTotal = 0;
        int updateSuccess = 0;

        for (Product sysProduct : sysProducts) {
            try {
                LazadaProductAndSysProduct productLink = lazadaProductAndSysProductRepository.findBySysProductId(sysProduct.getId()).orElse(null);
                Long itemId;
                if (productLink == null) {
                    itemId = createLazadaProductItem(ecomAccount, sysProduct, warehouse);
                    createTotal++;
                    if (itemId != null) createSuccess++;
                } else {
                    itemId = updateLazadaProductItem(ecomAccount, sysProduct, warehouse);
                    updateTotal++;
                    if (itemId != null) updateSuccess++;
                }
                if (itemId != null) {
                    itemIds.add(itemId);
                    syncProducts.add(sysProduct);
                }
            } catch (JsonProcessingException | ApiException e) {
                e.printStackTrace();
            }
        }

        // Fetch products to create links
        int fetchSuccess = 0;

        for (int i = 0; i < itemIds.size(); ++i) {
            try {
                boolean success = fetchLazadaProductItem(user, ecomAccount, itemIds.get(i), warehouse, syncProducts.get(i));
                if (success) fetchSuccess++;
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }

        logger.info("pushLazadaProducts success | ecomId = {}, pushTotal = {}, pushSuccess = {}, createTotal = {}, createSuccess = {}, updateTotal = {}, updateSuccess = {}, fetchSuccess = {}",
                ecomAccount.getId(), sysProducts.size(), itemIds.size(), createTotal, createSuccess, updateTotal, updateSuccess, fetchSuccess);

        return PushLazadaProductsReport.builder()
                .pushTotal(sysProducts.size())
                .pushSuccess(itemIds.size())
                .createTotal(createTotal)
                .createSuccess(createSuccess)
                .updateTotal(updateTotal)
                .updateSuccess(updateSuccess)
                .fetchSuccess(fetchSuccess).build();
    }

    private Long createLazadaProductItem(EcomAccount ecomAccount, Product sysProduct, Warehouse warehouse) throws JsonProcessingException, ApiException {
        // Migrate images
        List<String> images = sysProduct.getImages(true).stream()
                .map(ProductImage::getUrl).collect(Collectors.toList());

        LazadaMigrateImagesRequest migrateImagesRequest = new LazadaMigrateImagesRequest(images);

        LazadaMigrateImagesBatchResponse migrateImagesBatchResponse = LazadaImageAPI.migrateImages(
                ecomAccount.getAccessToken(), migrateImagesRequest);

        if (migrateImagesBatchResponse == null) {
            logger.error("pushLazadaProductItem fail | migrateImages fail | ecomId = {}, productId = {}",
                    ecomAccount.getId(), sysProduct.getId());
            return null;
        }

        LazadaMigrateImagesResponse migrateImagesResponse = LazadaImageAPI.getMigrateImages(
                ecomAccount.getAccessToken(), migrateImagesBatchResponse.getBatchId());

        if (migrateImagesResponse == null) {
            logger.error("pushLazadaProductItem fail | getMigrateImages fail | ecomId = {}, productId = {}",
                    ecomAccount.getId(), sysProduct.getId());
            return null;
        }

        images = migrateImagesResponse.getData().getImages().stream()
                .map(LazadaMigrateImagesResponse.Data.Image::getUrl).collect(Collectors.toList());

        // Create product
        LazadaCreateProductRequest request = toLazadaCreateProductRequest(sysProduct, images, warehouse);

        if (request == null) {
            logger.error("pushLazadaProductItem create request fail | ecomId = {}, productId = {}",
                    ecomAccount.getId(), sysProduct.getId());
            return null;
        }

        LazadaCreateProductResponse response = LazadaProductAPI.createProduct(ecomAccount.getAccessToken(), request);

        if (response == null) {
            logger.error("pushLazadaProductItem fail | ecomId = {}, productId = {}", ecomAccount.getId(), sysProduct.getId());
            return null;
        }

        logger.info("pushLazadaProductItem success | ecomId = {}, productId = {}, itemId = {}, skuCount = {}",
                ecomAccount.getId(), sysProduct.getId(), response.getData().getItemId(), response.getData().getSkus().size());

        return response.getData().getItemId();
    }

    private LazadaCreateProductRequest toLazadaCreateProductRequest(Product product, List<String> migratedImages, Warehouse warehouse) {
        if (product.getCategory() == null) {
            logger.error("toLazadaCreateProductRequest fail | category null | productId = {}", product.getId());
            return null;
        }

        LazadaCreateProductRequest req = new LazadaCreateProductRequest();

        LazadaCategoryAndSysCategory categoryLink = lazadaCategoryAndSysCategoryRepository.findByGeneralManagerIdAndSysCategoryId(
                product.getGeneralManager().getId(),
                product.getCategory().getId()
        ).orElse(null);

        if (categoryLink == null) {
            logger.error("toLazadaCreateProductRequest fail | category not link | productId = {}", product.getId());
            return null;
        }

        req.product = new LazadaCreateProductRequest.Product();

        // Category
        req.product.primaryCategory = categoryLink.getLazadaCategory().getLazadaCategoryId();

        // Images
        req.product.images = migratedImages;

        // Attributes
        Map<String, String> remainingMandatoryAttributes;
        try {
            LazadaGetCategoryAttributesResponse attributesRes = LazadaCategoryAPI.getCategoryAttributes(req.product.primaryCategory);
            if (attributesRes == null) {
                logger.error("toLazadaCreateProductRequest fail | attributeRes null | productId = {}", product.getId());
                return null;
            }
            remainingMandatoryAttributes = attributesRes.getRemainingMandatoryAttributes();
        } catch (ApiException e) {
            e.printStackTrace();
            logger.error("toLazadaCreateProductRequest fail | getCategoryAttributes fail | productId = {}", product.getId());
            return null;
        }

        req.product.attributes = new HashMap<>();
        req.product.attributes.put(LazadaCreateProductRequest.Attribute.NAME, product.getName());
        req.product.attributes.put(LazadaCreateProductRequest.Attribute.DESCRIPTION, product.getDescription());
        req.product.attributes.put(LazadaCreateProductRequest.Attribute.BRAND, LazadaCreateProductRequest.Attribute.BRAND_DEFAULT_VALUE);
        req.product.attributes.putAll(remainingMandatoryAttributes);

        // Variations
        req.product.variations = new HashMap<>();
        for (OptionModel option : product.getOptions(true)) {
            String key = LazadaCreateProductRequest.Variation.TAG_PREFIX + (req.product.variations.size() + 1);
            LazadaCreateProductRequest.Variation value = new LazadaCreateProductRequest.Variation();
            value.name = option.getProcessedName();
            value.hasImage = false;
            value.customize = true;
            value.options = option.getOptionValues(true).stream().map(OptionValue::getName).collect(Collectors.toList());
            req.product.variations.put(key, value);
        }

        // Skus
        req.product.skus = new ArrayList<>();
        for (Variant variant : product.getVariants(true)) {
            Map<String, Object> sku = new HashMap<>();

            sku.put(LazadaCreateProductRequest.Sku.SELLER_SKU, variant.getSku());
            sku.put(LazadaCreateProductRequest.Sku.QUANTITY, variant.getStockWarehouse(warehouse.getId()));

            sku.put(LazadaCreateProductRequest.Sku.PRICE, variant.getOriginalCost());
            if (!variant.getOriginalCost().equals(variant.getCost())) {
                sku.put(LazadaCreateProductRequest.Sku.SPECIAL_PRICE, variant.getCost());
                sku.put(LazadaCreateProductRequest.Sku.SPECIAL_FROM_DATE, LazadaCreateProductRequest.Sku.SPECIAL_FROM_DATE_DEFAULT_VALUE);
                sku.put(LazadaCreateProductRequest.Sku.SPECIAL_TO_DATE, LazadaCreateProductRequest.Sku.SPECIAL_TO_DATE_DEFAULT_VALUE);
            }

            sku.put(LazadaCreateProductRequest.Sku.PACKAGE_HEIGHT, variant.getProduct().getHeight());
            sku.put(LazadaCreateProductRequest.Sku.PACKAGE_LENGTH, variant.getProduct().getLength());
            sku.put(LazadaCreateProductRequest.Sku.PACKAGE_WIDTH, variant.getProduct().getWidth());
            sku.put(LazadaCreateProductRequest.Sku.PACKAGE_WEIGHT, variant.getProduct().getWeightLazada());

            for (VariantValue variantValue : variant.getVariantValues(true)) {
                String key = variantValue.getOptionValue().getOption().getProcessedName();
                String value = variantValue.getOptionValue().getName();
                sku.put(key, value);
            }

            req.product.skus.add(sku);
        }

        // Update Option's name
        product.getOptions().forEach(option -> {
            option.setName(option.getProcessedName());
            optionRepository.save(option);
        });

        return req;
    }

    private Long updateLazadaProductItem(EcomAccount ecomAccount, Product sysProduct, Warehouse warehouse) throws JsonProcessingException, ApiException {
        // Migrate images
        List<String> images = sysProduct.getImages(true).stream()
                .map(ProductImage::getUrl).collect(Collectors.toList());

        LazadaMigrateImagesRequest migrateImagesRequest = new LazadaMigrateImagesRequest(images);

        LazadaMigrateImagesBatchResponse migrateImagesBatchResponse = LazadaImageAPI.migrateImages(
                ecomAccount.getAccessToken(), migrateImagesRequest);

        if (migrateImagesBatchResponse == null) {
            logger.error("updateLazadaProductItem fail | migrateImages fail | ecomId = {}, productId = {}",
                    ecomAccount.getId(), sysProduct.getId());
            return null;
        }

        LazadaMigrateImagesResponse migrateImagesResponse = LazadaImageAPI.getMigrateImages(
                ecomAccount.getAccessToken(), migrateImagesBatchResponse.getBatchId());

        if (migrateImagesResponse == null) {
            logger.error("updateLazadaProductItem fail | getMigrateImages fail | ecomId = {}, productId = {}",
                    ecomAccount.getId(), sysProduct.getId());
            return null;
        }

        images = migrateImagesResponse.getData().getImages().stream()
                .map(LazadaMigrateImagesResponse.Data.Image::getUrl).collect(Collectors.toList());

        // Update product
        LazadaUpdateProductRequest request = toLazadaUpdateProductRequest(sysProduct, images, warehouse);

        if (request == null) {
            logger.error("updateLazadaProductItem create request fail | ecomId = {}, productId = {}",
                    ecomAccount.getId(), sysProduct.getId());
            return null;
        }

        boolean response = LazadaProductAPI.updateProduct(ecomAccount.getAccessToken(), request);

        if (!response) {
            logger.error("updateLazadaProductItem fail | ecomId = {}, productId = {}", ecomAccount.getId(), sysProduct.getId());
            return null;
        }

        logger.info("updateLazadaProductItem success | ecomId = {}, productId = {}, itemId = {}, skuCount = {}",
                ecomAccount.getId(), sysProduct.getId(), request.getProduct().getItemId(), request.getProduct().getSkus().size());

        return request.getProduct().getItemId();
    }

    private LazadaUpdateProductRequest toLazadaUpdateProductRequest(Product product, List<String> migratedImages, Warehouse warehouse) {
        if (product.getCategory() == null) {
            logger.error("toLazadaUpdateProductRequest fail | category null | productId = {}", product.getId());
            return null;
        }

        LazadaUpdateProductRequest req = new LazadaUpdateProductRequest();
        req.product = new LazadaUpdateProductRequest.Product();

        // ItemId
        LazadaProductAndSysProduct productLink = lazadaProductAndSysProductRepository.findBySysProductId(product.getId()).orElse(null);
        if (productLink == null) {
            logger.error("toLazadaUpdateProductRequest fail | product not link | productId = {}", product.getId());
            return null;
        }
        req.product.itemId = productLink.getLazadaProduct().getItemId();

        // Category
        LazadaCategoryAndSysCategory categoryLink = lazadaCategoryAndSysCategoryRepository.findByGeneralManagerIdAndSysCategoryId(
                product.getGeneralManager().getId(), product.getCategory().getId()).orElse(null);
        if (categoryLink == null) {
            logger.error("toLazadaUpdateProductRequest fail | category not link | productId = {}", product.getId());
            return null;
        }
        req.product.primaryCategory = categoryLink.getLazadaCategory().getLazadaCategoryId();

        // Images
        req.product.images = migratedImages;

        // Attributes
        req.product.attributes = new HashMap<>();
        req.product.attributes.put(LazadaCreateProductRequest.Attribute.NAME, product.getName());
        req.product.attributes.put(LazadaCreateProductRequest.Attribute.DESCRIPTION, product.getDescription());

        // Skus
        req.product.skus = new ArrayList<>();
        for (Variant variant : product.getVariants(true)) {
            Map<String, Object> sku = new HashMap<>();

            sku.put(LazadaCreateProductRequest.Sku.SELLER_SKU, variant.getSku());
            if (warehouse != null) {
                sku.put(LazadaCreateProductRequest.Sku.QUANTITY, variant.getStockWarehouse(warehouse.getId()));
            }

            sku.put(LazadaCreateProductRequest.Sku.PRICE, variant.getOriginalCost());
            sku.put(LazadaCreateProductRequest.Sku.SPECIAL_PRICE, variant.getCost());
            sku.put(LazadaCreateProductRequest.Sku.SPECIAL_FROM_DATE, LazadaCreateProductRequest.Sku.SPECIAL_FROM_DATE_DEFAULT_VALUE);
            sku.put(LazadaCreateProductRequest.Sku.SPECIAL_TO_DATE, LazadaCreateProductRequest.Sku.SPECIAL_TO_DATE_DEFAULT_VALUE);

            sku.put(LazadaCreateProductRequest.Sku.PACKAGE_HEIGHT, variant.getProduct().getHeight());
            sku.put(LazadaCreateProductRequest.Sku.PACKAGE_LENGTH, variant.getProduct().getLength());
            sku.put(LazadaCreateProductRequest.Sku.PACKAGE_WIDTH, variant.getProduct().getWidth());
            sku.put(LazadaCreateProductRequest.Sku.PACKAGE_WEIGHT, variant.getProduct().getWeightLazada());

            req.product.skus.add(sku);
        }

        return req;
    }

    @Override
    public boolean updateLazadaProduct(Long userId, Long productId) {
        Product product = productService.getProduct(userId, productId);
        LazadaProductAndSysProduct productLink = lazadaProductAndSysProductRepository.findBySysProductId(productId).orElse(null);

        if (productLink != null) {
            try {
                Long itemId = updateLazadaProductItem(productLink.getLazadaProduct().getEcomAccount(), product, null);
                if (itemId != null) {
                    logger.info("updateLazadaProduct success | productId = {}, itemId = {}", product, itemId);
                    return true;
                } else {
                    logger.error("updateLazadaProduct fail | request fail | productId = {}", product);
                }
            } catch (JsonProcessingException | ApiException e) {
                e.printStackTrace();
                logger.error("updateLazadaProduct fail | exception | productId = {}", product);
            }
        } else {
            logger.warn("updateLazadaProduct not execute | product not link | productId = {}", productId);
        }

        return false;
    }

    @Override
    public boolean pushLazadaVariantQuantity(Long userId, Long ecomId, Long variantId) {
        Variant variant = variantService.getVariant(userId, variantId);

        // itemId
        LazadaProductAndSysProduct productLink = lazadaProductAndSysProductRepository.findBySysProductId(variant.getProduct().getId()).orElse(null);
        if (productLink == null) {
            logger.error("pushLazadaVariantQuantity fail | product not link | userId = {}, ecomId = {}, variantId = {}",
                    userId, ecomId, variantId);
            return false;
        }
        Long itemId = productLink.getLazadaProduct().getItemId();

        // skuId
        LazadaVariantAndSysVariant variantLink = lazadaVariantAndSysVariantRepository.findBySysVariantId(variantId).orElse(null);
        if (variantLink == null) {
            logger.error("pushLazadaVariantQuantity fail | variant not link | userId = {}, ecomId = {}, variantId = {}",
                    userId, ecomId, variantId);
            return false;
        }
        Long skuId = variantLink.getLazadaVariant().getSkuId();

        // quantity
        LazadaSwwAndEcomAccount link = lazadaSwwAndEcomAccountRepository.getByEcomAccountId(ecomId);
        Warehouse warehouse = link.getSaleChannelShop().getWarehouse();
        Integer quantity = variant.getStockWarehouse(warehouse.getId());

        // Lazada API
        EcomAccount ecomAccount = ecomService.getEcomAccount(userId, ecomId);

        LazadaUpdatePriceQuantityRequest request = new LazadaUpdatePriceQuantityRequest(itemId, skuId, quantity);

        try {
            boolean success = LazadaProductAPI.updatePriceAndQuantity(ecomAccount.getAccessToken(), request);
            if (!success) {
                logger.error("pushLazadaVariantQuantity fail | response fail | userId = {}, ecomId = {}, variantId = {}", userId, ecomId, variantId);
                return false;
            }
        } catch (JsonProcessingException | ApiException e) {
            e.printStackTrace();
            logger.error("pushLazadaVariantQuantity fail | throw exception | userId = {}, ecomId = {}, variantId = {}", userId, ecomId, variantId);
            return false;
        }

        logger.info("pushLazadaVariantQuantity success | userId = {}, ecomId = {}, variantId = {}", userId, ecomId, variantId);
        return true;
    }

    @Override
    public Page<LazadaProduct> getLazadaProducts(Long userId, Long ecomId, Integer offset, Integer limit, String sortBy, String orderBy) {
        Long gmId = userId == null ? null : userRepository.getUserById(userId).getGeneralManager().getId();
        return lazadaProductRepository.findAll(
                LazadaProductSpecs.hasGeneralManagerId(gmId).and(LazadaProductSpecs.hasEcomAccountId(ecomId)),
                ZeusUtils.getDefaultPageable(offset, limit, sortBy, orderBy)
        );
    }

    @Override
    public LazadaProduct getLazadaProduct(Long userId, Long id) {
        Long gmId = userId == null ? null : userRepository.getUserById(userId).getGeneralManager().getId();
        return lazadaProductRepository.getOne(
                LazadaProductSpecs.hasGeneralManagerId(gmId).and(LazadaProductSpecs.hasId(id)));
    }

    @Override
    public Page<LazadaVariant> getLazadaVariants(Long userId, Long ecomId, Integer offset, Integer limit, String sortBy, String orderBy) {
        Long gmId = userId == null ? null : userRepository.getUserById(userId).getGeneralManager().getId();
        return lazadaVariantRepository.findAll(
                LazadaVariantSpecs.hasGeneralManagerId(gmId).and(LazadaVariantSpecs.hasEcomAccountId(ecomId)),
                ZeusUtils.getDefaultPageable(offset, limit, sortBy, orderBy)
        );
    }

    @Override
    public LazadaVariant getLazadaVariant(Long userId, Long id) {
        Long gmId = userId == null ? null : userRepository.getUserById(userId).getGeneralManager().getId();
        return lazadaVariantRepository.getOne(
                LazadaVariantSpecs.hasGeneralManagerId(gmId).and(LazadaVariantSpecs.hasId(id)));
    }
}
