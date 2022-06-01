package com.wifosell.zeus.service.impl;

import com.wifosell.zeus.model.attribute.Attribute;
import com.wifosell.zeus.model.category.Category;
import com.wifosell.zeus.model.option.OptionModel;
import com.wifosell.zeus.model.option.OptionValue;
import com.wifosell.zeus.model.product.Product;
import com.wifosell.zeus.model.product.ProductImage;
import com.wifosell.zeus.model.product.Variant;
import com.wifosell.zeus.model.product.VariantValue;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.payload.request.product.AddProductRequest;
import com.wifosell.zeus.payload.request.product.IProductRequest;
import com.wifosell.zeus.payload.request.product.UpdateProductRequest;
import com.wifosell.zeus.repository.*;
import com.wifosell.zeus.service.ProductService;
import com.wifosell.zeus.specs.ProductSpecs;
import com.wifosell.zeus.utils.ZeusUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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

    @Override
    public Page<Product> getProducts(
            Long userId,
            List<Long> warehouseIds,
            Integer minQuantity,
            Integer maxQuantity,
            List<Boolean> isActives,
            int offset,
            int limit,
            String sortBy,
            String orderBy
    ) {
        Long gmId = userId == null ? null : userRepository.getUserById(userId).getGeneralManager().getId();
        return productRepository.findAll(
                ProductSpecs.hasGeneralManager(gmId)
                        .and(ProductSpecs.inWarehouses(warehouseIds))
                        .and(ProductSpecs.hasQuantityBetween(minQuantity, maxQuantity))
                        .and(ProductSpecs.inIsActives(isActives)),
                ZeusUtils.getDefaultPageable(offset, limit, sortBy, orderBy)
        );
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
        Optional.ofNullable(request.getImages()).ifPresent(urls -> {
            productImageRepository.deleteAllByProductId(product.getId());
            product.getImages().clear();

            List<ProductImage> images = new ArrayList<>();
            for (String url : urls) {
                ProductImage image = ProductImage.builder()
                        .url(url)
                        .product(product).build();
                images.add(image);
            }
            product.getImages().addAll(images);

            productImageRepository.saveAll(images);
            productRepository.save(product);
        });

        // Attributes
        Optional.ofNullable(request.getAttributes()).ifPresent(attributeRequests -> {
            // Delete
            List<Long> requestAttributeIds = attributeRequests.stream()
                    .map(IProductRequest.AttributeRequest::getId).collect(Collectors.toList());
            List<Attribute> deletedAttributes = new ArrayList<>();
            product.getAttributes().forEach(attribute -> {
                if (!requestAttributeIds.contains(attribute.getId())) {
                    deletedAttributes.add(attribute);
                }
            });
            deletedAttributes.forEach(attribute -> {
                product.getAttributes().remove(attribute);
                attributeRepository.delete(attribute);
            });
            productRepository.save(product);

            // Add or update
            for (IProductRequest.AttributeRequest attributeRequest : attributeRequests) {
                Optional<Attribute> optionalAttribute = attributeRequest.getId() != null ?
                        attributeRepository.findById(attributeRequest.getId()) : Optional.empty();
                Attribute attribute;
                if (optionalAttribute.isPresent()) {
                    attribute = optionalAttribute.get();
                    attribute.setName(attributeRequest.getName());
                    attribute.setValue(attributeRequest.getValue());
                } else {
                    attribute = Attribute.builder()
                            .name(attributeRequest.getName())
                            .value(attributeRequest.getValue())
                            .product(product)
                            .build();
                    product.getAttributes().add(attribute);
                }
                attributeRepository.save(attribute);
            }
            productRepository.save(product);
        });

        // Options & Variants
        Optional.ofNullable(request.getOptions()).ifPresent(optionRequests -> {
            Optional.ofNullable(request.getVariants()).ifPresent(variantRequests -> {
                // Options
                optionRepository.deleteAllByProductId(product.getId());
                product.getOptions().clear();

                List<OptionModel> optionModels = new ArrayList<>();
                for (IProductRequest.OptionRequest optionRequest : optionRequests) {
                    OptionModel optionModel = OptionModel.builder()
                            .name(optionRequest.getName())
                            .product(product)
                            .generalManager(gm)
                            .build();
                    List<OptionValue> optionValues = new ArrayList<>();
                    for (String value : optionRequest.getValues()) {
                        OptionValue optionValue = OptionValue.builder()
                                .value(value)
                                .option(optionModel).build();
                        optionValues.add(optionValue);
                    }
                    optionModel.setOptionValues(optionValues);

                    optionModels.add(optionModel);

                    optionValueRepository.saveAll(optionValues);
                    optionRepository.save(optionModel);
                }
                product.getOptions().addAll(optionModels);

                productRepository.save(product);

                // Variants
                variantRepository.deleteAllByProductId(product.getId());
                product.getVariants().clear();
                this.genVariants(gm, product, product.getOptions(), variantRequests);
                productRepository.save(product);
            });
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
            Variant variant = Variant.builder()
                    .cost(new BigDecimal(variantRequest.getCost()))
                    .sku(variantRequest.getSku())
                    .barcode(variantRequest.getBarcode())
                    .product(product)
                    .generalManager(gm)
                    .build();

            List<VariantValue> variantValues = new ArrayList<>();
            for (OptionValue optionValue : combination) {
                VariantValue variantValue = VariantValue.builder()
                        .optionValue(optionValue)
                        .variant(variant).build();
                variantValues.add(variantValue);
            }

            variant.setVariantValues(variantValues);
            product.getVariants().add(variant);

            variantValueRepository.saveAll(variantValues);
            variantRepository.save(variant);
            return ++j;
        }

        for (OptionValue optionValue : options.get(i).getOptionValues()) {
            combination.set(k, optionValue);
            j = genVariants(gm, product, options, variantRequests, combination, i + 1, j, k + 1);
        }

        return j;
    }
}

