package com.wifosell.zeus.service.impl;

import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.exception.AppException;
import com.wifosell.zeus.model.attribute.Attribute;
import com.wifosell.zeus.model.category.Category;
import com.wifosell.zeus.model.option.OptionModel;
import com.wifosell.zeus.model.option.OptionValue;
import com.wifosell.zeus.model.product.*;
import com.wifosell.zeus.model.stock.Stock_;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.model.user.User_;
import com.wifosell.zeus.model.warehouse.Warehouse_;
import com.wifosell.zeus.payload.GApiErrorBody;
import com.wifosell.zeus.utils.paging.PageInfo;
import com.wifosell.zeus.payload.request.product.AddProductRequest;
import com.wifosell.zeus.payload.request.product.IProductRequest;
import com.wifosell.zeus.payload.request.product.UpdateProductRequest;
import com.wifosell.zeus.repository.*;
import com.wifosell.zeus.service.ProductService;
import com.wifosell.zeus.specs.ProductSpecs;
import com.wifosell.zeus.utils.ZeusUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Transactional
@Service("ProductService")
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final AttributeRepository attributeRepository;
    private final OptionRepository optionRepository;
    private final OptionValueRepository optionValueRepository;
    private final VariantRepository variantRepository;
    private final VariantValueRepository variantValueRepository;
    private final UserRepository userRepository;
    private final ProductImageRepository productImageRepository;
    private final StockRepository stockRepository;
    private final EntityManager entityManager;

    @Override
    public Page<Product> getProducts(
            Long userId,
            List<Long> warehouseIds,
            Integer minQuantity,
            Integer maxQuantity,
            List<Boolean> isActives,
            Integer offset,
            Integer limit,
            String sortBy,
            String orderBy
    ) {
        Long gmId = userId == null ? null : userRepository.getUserById(userId).getGeneralManager().getId();
        return productRepository.findAll(
                ProductSpecs.hasGeneralManager(gmId)
                        .and(ProductSpecs.hasStocks(warehouseIds, minQuantity, maxQuantity))
                        .and(ProductSpecs.inIsActives(isActives)),
                ZeusUtils.getDefaultPageable(offset, limit, sortBy, orderBy)
        );
    }

    @Override
    public PageInfo<Product> searchProducts(
            Long userId,
            String keyword,
            List<Long> warehouseIds,
            Integer minQuantity,
            Integer maxQuantity,
            List<Boolean> isActives,
            Integer offset,
            Integer limit
    ) {
        SearchSession searchSession = Search.session(entityManager);

        Long gmId = userId == null ? null : userRepository.getUserById(userId).getGeneralManager().getId();
        if (offset == null) {
            offset = 0;
        }
        if (limit == null || limit > 100) {
            limit = 100;
        }

        SearchResult<Product> result = searchSession.search(Product.class).where(f -> f.bool(b -> {
            b.must(f.matchAll());
            if (gmId != null) {
                b.must(f.match().field(Product_.GENERAL_MANAGER + "." + User_.ID).matching(gmId));
            }
            if (keyword != null && !keyword.isEmpty()) {
                b.must(f.match().fields(Product_.VARIANTS + "." + Variant_.SKU, Product_.NAME).matching(keyword));
            }
            b.must(f.nested().objectField(Product_.VARIANTS + "." + Variant_.STOCKS).nest(f.bool(c -> {
                c.must(f.matchAll());
                if (warehouseIds != null) {
                    c.must(f.terms().field(Product_.VARIANTS + "." + Variant_.STOCKS + "." + Stock_.WAREHOUSE + "." + Warehouse_.ID).matchingAny(warehouseIds));
                }
                if (minQuantity != null) {
                    c.must(f.range().field(Product_.VARIANTS + "." + Variant_.STOCKS + "." + Stock_.QUANTITY).atLeast(minQuantity));
                }
                if (maxQuantity != null) {
                    c.must(f.range().field(Product_.VARIANTS + "." + Variant_.STOCKS + "." + Stock_.QUANTITY).atMost(maxQuantity));
                }
            })));
            if (isActives == null || isActives.isEmpty()) {
                b.must(f.match().field(Product_.IS_ACTIVE).matching(true));
            } else {
                b.must(f.terms().field(Product_.IS_ACTIVE).matchingAny(isActives));
            }
        })).fetch(offset * limit, limit);

        return new PageInfo<>(result.hits(), offset, limit, result.total().hitCount());
    }

    @Override
    public Product getProduct(
            Long userId,
            @NonNull Long productId
    ) {
        Long gmId = userId == null ? null : userRepository.getUserById(userId).getGeneralManager().getId();
        return productRepository.getOne(
                ProductSpecs.hasGeneralManager(gmId)
                        .and(ProductSpecs.hasId(productId))
        );
    }

    @Override
    public Product addProduct(
            @NonNull Long userId,
            @Valid AddProductRequest request
    ) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        Product product = new Product();
        return this.updateProductByRequest(product, request, gm);
    }

    @Override
    public Product updateProduct(@NonNull Long userId, @NonNull Long productId, @Valid UpdateProductRequest request) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        Product product = getProduct(userId, productId);
        return this.updateProductByRequest(product, request, gm);
    }

    @Override
    public Product activateProduct(Long userId, @NonNull Long productId) {
        Product product = getProduct(userId, productId);
        product.setIsActive(true);
        return productRepository.save(product);
    }

    @Override
    public Product deactivateProduct(Long userId, @NonNull Long productId) {
        Product product = getProduct(userId, productId);
        product.setIsActive(false);
        return productRepository.save(product);
    }

    @Override
    public List<Product> activateProducts(Long userId, @NonNull List<Long> productIds) {
        return productIds.stream().map(id -> this.activateProduct(userId, id)).collect(Collectors.toList());
    }

    @Override
    public List<Product> deactivateProducts(Long userId, @NonNull List<Long> productIds) {
        return productIds.stream().map(id -> this.deactivateProduct(userId, id)).collect(Collectors.toList());
    }

    private Product updateProductByRequest(Product product, IProductRequest request, User gm) {
        Optional.ofNullable(request.getName()).ifPresent(product::setName);
        Optional.ofNullable(request.getDescription()).ifPresent(product::setDescription);
        Optional.ofNullable(request.getCategoryId()).ifPresent(categoryId -> {
            Category category = categoryRepository.getById(categoryId);
            product.setCategory(category);
        });
        Optional.ofNullable(request.getWeight()).ifPresent(product::setWeight);
        Optional.ofNullable(request.getDimension()).ifPresent(product::setDimension);
        Optional.ofNullable(request.getState()).ifPresent(product::setState);
        Optional.ofNullable(request.getStatus()).ifPresent(product::setStatus);

        // Images
        Optional.ofNullable(request.getImages()).ifPresent(imageRequests -> {
            if (this.haveDuplicatedIds(imageRequests.stream().map(IProductRequest.ImageRequest::getId).collect(Collectors.toList()))) {
                throw new AppException(GApiErrorBody.makeErrorBody(
                        EAppExceptionCode.REQUEST_PAYLOAD_FORMAT_ERROR,
                        "Image id must be unique.",
                        imageRequests
                ));
            }

            List<ProductImage> images = product.getImages().stream()
                    .filter(image -> !image.isDeleted())
                    .collect(Collectors.toList());

            images.forEach(image -> {
                IProductRequest.ImageRequest existingImageRequest = null;

                for (IProductRequest.ImageRequest imageRequest : imageRequests) {
                    if (image.getId().equals(imageRequest.getId())) {
                        image.setUrl(imageRequest.getUrl());
                        productImageRepository.save(image);
                        existingImageRequest = imageRequest;
                        break;
                    }
                }

                if (existingImageRequest == null) {
                    image.setDeleted(true);
                    productImageRepository.save(image);
                } else {
                    imageRequests.remove(existingImageRequest);
                }
            });

            for (IProductRequest.ImageRequest imageRequest : imageRequests) {
                ProductImage image = ProductImage.builder()
                        .url(imageRequest.getUrl())
                        .product(product)
                        .build();
                productImageRepository.save(image);
                product.getImages().add(image);
            }

            productRepository.save(product);
        });

        // Attributes
        Optional.ofNullable(request.getAttributes()).ifPresent(attributeRequests -> {
            if (this.haveDuplicatedIds(attributeRequests.stream().map(IProductRequest.AttributeRequest::getId).collect(Collectors.toList()))) {
                throw new AppException(GApiErrorBody.makeErrorBody(EAppExceptionCode.REQUEST_PAYLOAD_FORMAT_ERROR, "Attribute id must be unique.", attributeRequests));
            }

            List<Attribute> deletedAttributes = new ArrayList<>();

            product.getAttributes().forEach(attribute -> {
                IProductRequest.AttributeRequest existingAttributeRequest = null;
                for (IProductRequest.AttributeRequest attributeRequest : attributeRequests) {
                    if (attribute.getId().equals(attributeRequest.getId())) {
                        attribute.setName(attributeRequest.getName());
                        attribute.setValue(attributeRequest.getValue());
                        attributeRepository.save(attribute);
                        existingAttributeRequest = attributeRequest;
                        break;
                    }
                }
                if (existingAttributeRequest == null) {
                    deletedAttributes.add(attribute);
                } else {
                    attributeRequests.remove(existingAttributeRequest);
                }
            });

            deletedAttributes.forEach(attribute -> {
                product.getAttributes().remove(attribute);
                attributeRepository.delete(attribute);
            });

            for (IProductRequest.AttributeRequest attributeRequest : attributeRequests) {
                Attribute attribute = Attribute.builder()
                        .name(attributeRequest.getName())
                        .value(attributeRequest.getValue())
                        .product(product)
                        .build();
                attributeRepository.save(attribute);
                product.getAttributes().add(attribute);
            }

            productRepository.save(product);
        });

        // Options
        Optional.ofNullable(request.getOptions()).ifPresent(optionRequests -> {
            if (this.haveDuplicatedIds(optionRequests.stream().map(IProductRequest.OptionRequest::getId).collect(Collectors.toList()))) {
                throw new AppException(GApiErrorBody.makeErrorBody(EAppExceptionCode.REQUEST_PAYLOAD_FORMAT_ERROR, "Option id must be unique.", optionRequests));
            }

            optionRequests.forEach(optionRequest -> {
                if (this.haveDuplicatedIds(optionRequest.getValues().stream().map(IProductRequest.OptionValueRequest::getId).collect(Collectors.toList()))) {
                    throw new AppException(GApiErrorBody.makeErrorBody(EAppExceptionCode.REQUEST_PAYLOAD_FORMAT_ERROR, "OptionValue id must be unique.", optionRequest.getValues()));
                }
            });

            List<OptionModel> options = product.getOptions().stream()
                    .filter(option -> !option.isDeleted())
                    .collect(Collectors.toList());

            boolean isOptionIdsUnchanged = options.size() != 0 && options.stream().allMatch(option -> {
                for (IProductRequest.OptionRequest optionRequest : optionRequests) {
                    if (option.getId().equals(optionRequest.getId()))
                        return true;
                }
                return false;
            });

            if (isOptionIdsUnchanged) {
                options.forEach(option -> {
                    for (IProductRequest.OptionRequest optionRequest : optionRequests) {
                        if (option.getId().equals(optionRequest.getId())) {
                            option.setName(optionRequest.getName());

                            List<OptionValue> optionValues = option.getOptionValues().stream()
                                    .filter(optionValue -> !optionValue.isDeleted())
                                    .collect(Collectors.toList());

                            optionValues.forEach(optionValue -> {
                                IProductRequest.OptionValueRequest existingOptionValueRequest = null;

                                for (IProductRequest.OptionValueRequest optionValueRequest : optionRequest.getValues()) {
                                    if (optionValue.getId().equals(optionValueRequest.getId())) {
                                        optionValue.setName(optionValueRequest.getName());
                                        optionValueRepository.save(optionValue);
                                        existingOptionValueRequest = optionValueRequest;
                                        break;
                                    }
                                }

                                if (existingOptionValueRequest == null) {
                                    optionValue.getVariantValues().stream()
                                            .map(VariantValue::getVariant)
                                            .forEach(variant -> {
                                                variant.getVariantValues().forEach(variantValue -> {
                                                    variantValue.setDeleted(true);
                                                    variantValueRepository.save(variantValue);
                                                });
                                                variant.getStocks().forEach(stock -> {
                                                    stock.setDeleted(true);
                                                    stockRepository.save(stock);
                                                });
                                                variant.setDeleted(true);
                                                variantRepository.save(variant);
                                            });
                                    optionValue.setDeleted(true);
                                    optionValueRepository.save(optionValue);
                                } else {
                                    optionRequest.getValues().remove(existingOptionValueRequest);
                                }
                            });

                            for (IProductRequest.OptionValueRequest optionValueRequest : optionRequest.getValues()) {
                                OptionValue optionValue = OptionValue.builder()
                                        .name(optionValueRequest.getName())
                                        .option(option)
                                        .build();
                                optionValueRepository.save(optionValue);
                                option.getOptionValues().add(optionValue);
                            }

                            optionRepository.save(option);
                            break;
                        }
                    }
                });
            } else {
                product.getOptions().forEach(option -> {
                    option.getOptionValues().forEach(optionValue -> {
                        optionValue.setDeleted(true);
                        optionValueRepository.save(optionValue);
                    });
                    option.setDeleted(true);
                    optionRepository.save(option);
                });

                product.getVariants().forEach(variant -> {
                    variant.getVariantValues().forEach(variantValue -> {
                        variantValue.setDeleted(true);
                        variantValueRepository.save(variantValue);
                    });
                    variant.setDeleted(true);
                    variantRepository.save(variant);
                });

                optionRequests.forEach(optionRequest -> {
                    OptionModel option = OptionModel.builder()
                            .name(optionRequest.getName())
                            .product(product)
                            .generalManager(gm)
                            .build();

                    optionRequest.getValues().forEach(optionValueRequest -> {
                        OptionValue optionValue = OptionValue.builder()
                                .name(optionValueRequest.getName())
                                .option(option)
                                .build();
                        option.getOptionValues().add(optionValue);
                    });

                    product.getOptions().add(option);

                    optionValueRepository.saveAll(option.getOptionValues());
                    optionRepository.save(option);
                    productRepository.save(product);
                });
            }
        });

        Optional.ofNullable(request.getVariants()).ifPresent(variantRequests -> {
            if (this.haveDuplicatedIds(variantRequests.stream().map(IProductRequest.VariantRequest::getId).collect(Collectors.toList()))) {
                throw new AppException(GApiErrorBody.makeErrorBody(
                        EAppExceptionCode.REQUEST_PAYLOAD_FORMAT_ERROR,
                        "Variant id must be unique.",
                        variantRequests)
                );
            }

            List<OptionModel> options = product.getOptions().stream()
                    .filter(option -> !option.isDeleted())
                    .collect(Collectors.toList());

            int variantNum = 1;
            for (OptionModel option : options) {
                variantNum *= option.getOptionValues().stream().filter(optionValue -> !optionValue.isDeleted()).count();
            }
            if (variantNum != variantRequests.size()) {
                throw new AppException(GApiErrorBody.makeErrorBody(
                        EAppExceptionCode.REQUEST_PAYLOAD_FORMAT_ERROR,
                        String.format("There must be %d variants instead of %d.", variantNum, variantRequests.size()),
                        variantRequests)
                );
            }

            List<Variant> variants = product.getVariants().stream()
                    .filter(variant -> !variant.isDeleted())
                    .collect(Collectors.toList());

            boolean isValidVariantRequests = variants.stream()
                    .allMatch(variant -> variantRequests.stream()
                            .map(IProductRequest.VariantRequest::getId)
                            .collect(Collectors.toList())
                            .contains(variant.getId())
                    );

            if (!isValidVariantRequests) {
                throw new AppException(GApiErrorBody.makeErrorBody(
                        EAppExceptionCode.REQUEST_PAYLOAD_FORMAT_ERROR,
                        "The variants updated have wrong id. All updated variants must have ids: " +
                                variants.stream().map(Variant::getId).collect(Collectors.toList()),
                        variantRequests)
                );
            }

            this.genVariants(gm, product, options, variantRequests);

            productRepository.save(product);
        });

        Optional.ofNullable(request.getIsActive()).ifPresent(product::setIsActive);
        product.setGeneralManager(gm);

        return productRepository.save(product);
    }

    private void genVariants(User gm, Product product, List<OptionModel> options, List<IProductRequest.VariantRequest> variantRequests) {
        List<OptionValue> combination = Arrays.asList(new OptionValue[options.size()]);
        int i = 0, j = 0, k = 0;
        this.genVariants(gm, product, options, variantRequests, combination, i, j, k);
    }

    private int genVariants(User gm, Product product, List<OptionModel> options, List<IProductRequest.VariantRequest> variantRequests, List<OptionValue> combination, int i, int j, int k) {
        if (i == options.size()) {
            IProductRequest.VariantRequest variantRequest = variantRequests.get(j);

            List<Variant> variants = product.getVariants().stream()
                    .filter(variant -> !variant.isDeleted())
                    .collect(Collectors.toList());

            boolean isExistingVariant = false;

            for (Variant variant : variants) {
                if (variant.getId().equals(variantRequest.getId())) {
                    variant.setCost(new BigDecimal(variantRequest.getCost()));
                    variant.setSku(variantRequest.getSku());
                    variant.setBarcode(variantRequest.getBarcode());
                    variantRepository.save(variant);
                    isExistingVariant = true;
                    break;
                }
            }

            if (!isExistingVariant) {
                Variant variant = Variant.builder()
                        .cost(new BigDecimal(variantRequest.getCost()))
                        .sku(variantRequest.getSku())
                        .barcode(variantRequest.getBarcode())
                        .product(product)
                        .generalManager(gm)
                        .build();

                for (OptionValue optionValue : combination) {
                    VariantValue variantValue = VariantValue.builder()
                            .optionValue(optionValue)
                            .variant(variant).build();
                    variant.getVariantValues().add(variantValue);
                }
                product.getVariants().add(variant);

                variantValueRepository.saveAll(variant.getVariantValues());
                variantRepository.save(variant);
                productRepository.save(product);
            }

            return ++j;
        }

        List<OptionValue> optionValues = options.get(i).getOptionValues().stream()
                .filter(optionValue -> !optionValue.isDeleted())
                .collect(Collectors.toList());

        for (OptionValue optionValue : optionValues) {
            combination.set(k, optionValue);
            j = genVariants(gm, product, options, variantRequests, combination, i + 1, j, k + 1);
        }

        return j;
    }

    private boolean haveDuplicatedIds(List<Long> ids) {
        Set<Long> set = new HashSet<>();
        for (Long id : ids) {
            if (set.contains(id))
                return true;
            if (id != null)
                set.add(id);
        }
        return false;
    }
}

