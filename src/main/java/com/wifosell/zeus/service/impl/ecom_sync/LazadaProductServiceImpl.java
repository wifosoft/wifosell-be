package com.wifosell.zeus.service.impl.ecom_sync;

import com.lazada.lazop.util.ApiException;
import com.wifosell.lazada.modules.product.LazadaProductAPI;
import com.wifosell.lazada.modules.product.payload.LazadaGetProductItemResponse;
import com.wifosell.lazada.modules.product.payload.LazadaGetProductsResponse;
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
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.model.warehouse.Warehouse;
import com.wifosell.zeus.payload.GApiErrorBody;
import com.wifosell.zeus.payload.provider.lazada.report.FetchAndSyncLazadaProductsReport;
import com.wifosell.zeus.payload.request.product.IProductRequest;
import com.wifosell.zeus.payload.request.product.UpdateProductRequest;
import com.wifosell.zeus.repository.CategoryRepository;
import com.wifosell.zeus.repository.ProductImageRepository;
import com.wifosell.zeus.repository.ProductRepository;
import com.wifosell.zeus.repository.UserRepository;
import com.wifosell.zeus.repository.ecom_sync.*;
import com.wifosell.zeus.service.EcomService;
import com.wifosell.zeus.service.LazadaProductService;
import com.wifosell.zeus.service.ProductService;
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
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service("LazadaProductService")
@Transactional
@RequiredArgsConstructor
public class LazadaProductServiceImpl implements LazadaProductService {
    private static final Logger logger = LoggerFactory.getLogger(LazadaProductServiceImpl.class);

    private final UserRepository userRepository;

    private final EcomService ecomService;
    private final EcomAccountRepository ecomAccountRepository;
    private final LazadaSwwAndEcomAccountRepository lazadaSwwAndEcomAccountRepository;

    private final LazadaCategoryRepository lazadaCategoryRepository;
    private final LazadaProductRepository lazadaProductRepository;
    private final LazadaVariantRepository lazadaVariantRepository;

    private final LazadaCategoryAndSysCategoryRepository lazadaCategoryAndSysCategoryRepository;
    private final LazadaProductAndSysProductRepository lazadaProductAndSysProductRepository;
    private final LazadaVariantAndSysVariantRepository lazadaVariantAndSysVariantRepository;

    private final CategoryRepository categoryRepository;
    private final ProductService productService;
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;

    @Override
    public FetchAndSyncLazadaProductsReport fetchAndSyncLazadaProducts(Long userId, Long ecomId) {
        User user = userRepository.getUserById(userId);
        EcomAccount ecomAccount = ecomService.getEcomAccount(userId, ecomId);

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

            res.getData().getProducts().forEach(product -> {
                try {
                    boolean success = fetchAndSyncLazadaProductItem(user, ecomAccount, product.getItemId());
                    if (success) fetchSuccess.getAndIncrement();
                } catch (ApiException e) {
                    e.printStackTrace();
                } finally {
                    fetchTotal.getAndIncrement();
                }
            });

            if (fetchTotal.get() >= res.getData().getTotalProducts()) break;

            offset += res.getData().getProducts().size();
        }

        LazadaSwwAndEcomAccount link = lazadaSwwAndEcomAccountRepository.getByEcomAccountId(ecomId);
        Warehouse warehouse = link.getSaleChannelShop().getWarehouse();

        logger.info("getProducts success | ecomId = {}, fetchTotal = {}, fetchSuccess = {}",
                ecomAccount.getId(), fetchTotal, fetchSuccess);
        return FetchAndSyncLazadaProductsReport.builder().fetchTotal(fetchTotal.get()).fetchSuccess(fetchSuccess.get()).build();
    }

    private boolean fetchAndSyncLazadaProductItem(User user, EcomAccount ecomAccount, Long lazadaProductId) throws ApiException {
        LazadaGetProductItemResponse res = LazadaProductAPI.getProductItem(ecomAccount.getAccessToken(), lazadaProductId);

        if (res == null) {
            logger.error("getProductItem fail | ecomId = {}, productId = {}", ecomAccount.getId(), lazadaProductId);
            return false;
        }

        LazadaGetProductItemResponse.Data data = res.getData();

        // Create/Update LazadaProduct
        LazadaCategory lazadaCategory = lazadaCategoryRepository.findByLazadaCategoryId(data.getPrimaryCategoryId())
                .orElse(null);

        if (lazadaCategory == null) {
            logger.error("getProductItem fail | Lazada category not found | ecomId = {}, productId = {}, lazadaCategoryId = {}",
                    ecomAccount.getId(), lazadaProductId, data.getPrimaryCategoryId());
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
        Product sysProduct = productLink != null ? productLink.getSysProduct() : new Product();
        sysProduct = productService.updateProductByRequest(
                sysProduct,
                fromLazadaGetProductItemResponseData(data, sysProduct),
                user.getGeneralManager());

        LazadaCategoryAndSysCategory categoryLink = lazadaCategoryAndSysCategoryRepository.findByGeneralManagerIdAndLazadaCategoryId(
                ecomAccount.getGeneralManager().getId(), lazadaCategory.getId()).orElse(null);
        Category sysCategory = categoryLink != null ? categoryLink.getSysCategory() : null;
        sysProduct.setCategory(sysCategory);

        productRepository.save(sysProduct);

        // Link LazadaProduct and SysProduct
        if (productLink == null) {
            productLink = new LazadaProductAndSysProduct();
            productLink.setLazadaProduct(lazadaProduct);
            productLink.setSysProduct(sysProduct);
            productLink.setGeneralManager(user.getGeneralManager());
            lazadaProductAndSysProductRepository.save(productLink);
        }

        // Link LazadaVariants and SysVariants
        List<Variant> sysVariants = sysProduct.getVariants().stream().filter(v -> !v.isDeleted())
                .sorted(Comparator.comparing(Variant::getIndex))
                .collect(Collectors.toList());

        for (int i = 0; i < sysVariants.size(); ++i) {
            LazadaVariantAndSysVariant variantLink = lazadaVariantAndSysVariantRepository.findBySysVariantId(sysVariants.get(i).getId())
                    .orElse(new LazadaVariantAndSysVariant());
            variantLink.setSysVariant(sysVariants.get(i));
            variantLink.setLazadaVariant(lazadaVariants.get(i));
            lazadaVariantAndSysVariantRepository.save(variantLink);
        }

        // Update stock
        // TODO haukc

        logger.info("getProductItem success | ecomId = {}, itemId = {}, name = {}, skuCount = {}",
                ecomAccount.getId(), lazadaProduct.getItemId(), lazadaProduct.getName(), lazadaProduct.getSkuCount());
        return true;
    }

    private UpdateProductRequest fromLazadaGetProductItemResponseData(LazadaGetProductItemResponse.Data data, Product product) {
        UpdateProductRequest req = new UpdateProductRequest();

        // Metadata
        req.setName(data.getName());
        req.setDescription(data.getDescription());

        req.setWeight(data.getSkus().get(0).getPackageWeight());
        req.setLength(data.getSkus().get(0).getPackageLength());
        req.setWidth(data.getSkus().get(0).getPackageWidth());
        req.setHeight(data.getSkus().get(0).getPackageHeight());

        req.setIsActive(data.isActive());

        // Images
        List<ProductImage> productImages = product.getImages().stream().filter(i -> !i.isDeleted()).collect(Collectors.toList());
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
        List<Attribute> attributes = product.getAttributes().stream().filter(a -> !a.isDeleted()).collect(Collectors.toList());
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
        List<OptionModel> options = product.getOptions().stream().filter(o -> !o.isDeleted()).collect(Collectors.toList());

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
