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
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
@Service("Product")
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
    private final ShopRepository shopRepository;
    private final ProductImageRepository productImageRepository;

    @Override
    public Page<Product> getAllProducts(Boolean isActive, int offset, int limit, String sortBy, String orderBy) {
        Pageable pageable = PageRequest.of(offset, limit, Sort.by(Sort.Direction.fromString(orderBy), sortBy));
        if (isActive == null)
            return productRepository.findAll(pageable);
        return productRepository.findAndPaginateAllWithActive(isActive, pageable);
    }

    @Override
    public Page<Product> getProducts(@NonNull Long userId, Boolean isActive, int offset, int limit, String sortBy, String orderBy) {
        Pageable pageable = PageRequest.of(offset, limit, Sort.by(Sort.Direction.fromString(orderBy), sortBy));
        User gm = userRepository.getUserById(userId).getGeneralManager();
        if (isActive == null)
            return productRepository.findAndPaginateAllWithGm(gm.getId(), pageable);
        return productRepository.findAndPaginateAllWithGmAndActive(gm.getId(), isActive, pageable);
    }

//    @Override
//    public List<Product> getProductsByShopId(@NonNull Long userId, @NonNull Long shopId, Boolean isActive) {
//        User gm = userRepository.getUserById(userId).getGeneralManager();
//        Shop shop = shopRepository.getByIdWithGm(gm.getId(), shopId);
//        Stream<Product> productStream = shop.getProductShopRelations().stream().map(ProductShopRelation::getProduct);
//        if (isActive != null)
//            productStream = productStream.filter(product -> product.isActive() == isActive);
//        return productStream.collect(Collectors.toList());
//    }

    @Override
    public Product getProduct(@NonNull Long userId, @NonNull Long productId) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        return productRepository.getByIdWithGm(gm.getId(), productId);
    }

    @Override
    public Product addProduct(@NonNull Long userId, @Valid AddProductRequest request) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        Product product = new Product();
        return this.updateProductByRequest(product, request, gm);
    }

    @Override
    public Product updateProduct(@NonNull Long userId, @NonNull Long productId, @Valid UpdateProductRequest request) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        Product product = productRepository.getByIdWithGm(gm.getId(), productId);
        return this.updateProductByRequest(product, request, gm);
    }

    @Override
    public Product activateProduct(@NonNull Long userId, @NonNull Long productId) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        Product product = productRepository.getByIdWithGm(gm.getId(), productId);
        product.setIsActive(true);
        return productRepository.save(product);
    }

    @Override
    public Product deactivateProduct(@NonNull Long userId, @NonNull Long productId) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        Product product = productRepository.getByIdWithGm(gm.getId(), productId);
        product.setIsActive(false);
        return productRepository.save(product);
    }

    @Override
    public List<Product> activateProducts(@NonNull Long userId, @NonNull List<Long> productIds) {
        return productIds.stream().map(id -> this.activateProduct(userId, id)).collect(Collectors.toList());
    }

    @Override
    public List<Product> deactivateProducts(@NonNull Long userId, @NonNull List<Long> productIds) {
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
            attributeRepository.deleteAllByProductId(product.getId());
            product.getAttributes().clear();

            List<Attribute> attributes = new ArrayList<>();
            for (IProductRequest.AttributeRequest attributeRequest : attributeRequests) {
                Attribute attribute = Attribute.builder()
                        .name(attributeRequest.getName())
                        .value(attributeRequest.getValue())
                        .product(product)
                        .build();
                attributes.add(attribute);
            }
            product.getAttributes().addAll(attributes);

            attributeRepository.saveAll(attributes);
            productRepository.save(product);
        });

        // Options & Variants
        Optional.ofNullable(request.getOptions()).ifPresent(optionRequests -> {
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
        });

        // Variants
        Optional.ofNullable(request.getVariants()).ifPresent(variantRequests -> {
            variantRepository.deleteAllByProductId(product.getId());
            product.getVariants().clear();
            this.genVariants(product, product.getOptions(), variantRequests);
            productRepository.save(product);
        });

        Optional.ofNullable(request.getActive()).ifPresent(product::setIsActive);
        product.setGeneralManager(gm);

        return productRepository.save(product);
    }

    private void genVariants(Product product, List<OptionModel> options, List<IProductRequest.VariantRequest> variantRequests) {
        List<OptionValue> combination = Arrays.asList(new OptionValue[options.size()]);
        int i = 0, j = 0, k = 0;
        this.genVariants(product, options, variantRequests, combination, i, j, k);
    }

    private int genVariants(Product product, List<OptionModel> options, List<IProductRequest.VariantRequest> variantRequests, List<OptionValue> combination, int i, int j, int k) {
        if (i == options.size()) {
            IProductRequest.VariantRequest variantRequest = variantRequests.get(j);
            Variant variant = Variant.builder()
                    .cost(new BigDecimal(variantRequest.getCost()))
                    .sku(variantRequest.getSku())
                    .barcode(variantRequest.getBarcode())
                    .product(product).build();

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
            j = genVariants(product, options, variantRequests, combination, i + 1, j, k + 1);
        }

        return j;
    }
}

