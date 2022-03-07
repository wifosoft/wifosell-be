package com.wifosell.zeus.service.impl;

import com.wifosell.zeus.model.attribute.Attribute;
import com.wifosell.zeus.model.category.Category;
import com.wifosell.zeus.model.option.OptionModel;
import com.wifosell.zeus.model.option.OptionValue;
import com.wifosell.zeus.model.product.Product;
import com.wifosell.zeus.model.product.Variant;
import com.wifosell.zeus.model.product.VariantValue;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.payload.request.product.ProductRequest;
import com.wifosell.zeus.repository.*;
import com.wifosell.zeus.service.ProductService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service("Product")
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final AttributeRepository attributeRepository;
    private final OptionRepository optionRepository;
    private final OptionValueRepository optionValueRepository;
    private final VariantRepository variantRepository;
    private final VariantValueRepository variantValueRepository;
    private final UserRepository userRepository;

    @Autowired
    public ProductServiceImpl(
            ProductRepository productRepository,
            CategoryRepository categoryRepository,
            AttributeRepository attributeRepository,
            OptionRepository optionRepository,
            OptionValueRepository optionValueRepository,
            VariantRepository variantRepository,
            VariantValueRepository variantValueRepository,
            UserRepository userRepository
    ) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.attributeRepository = attributeRepository;
        this.optionRepository = optionRepository;
        this.optionValueRepository = optionValueRepository;
        this.variantRepository = variantRepository;
        this.variantValueRepository = variantValueRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Product> getAllProducts(Boolean isActive) {
        if (isActive == null)
            return productRepository.findAll();
        return productRepository.findAllWithActive(isActive);
    }

    @Override
    public List<Product> getProducts(@NonNull Long userId, Boolean isActive) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        if (isActive == null)
            return productRepository.findAllWithGm(gm.getId());
        return productRepository.findAllWithGmAndActive(gm.getId(), isActive);
    }

    @Override
    public Product getProduct(@NonNull Long userId, @NonNull Long productId) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        return productRepository.getByIdWithGm(gm.getId(), productId);
    }

    @Override
    public Product addProduct(@NonNull Long userId, @NonNull ProductRequest request) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        Product product = new Product();
        return this.updateProductByRequest(product, request, gm);
    }

    @Override
    public Product updateProduct(@NonNull Long userId, @NonNull Long productId, @NonNull ProductRequest request) {
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

    private Product updateProductByRequest(Product product, ProductRequest request, User gm) {
        Optional.ofNullable(request.getName()).ifPresent(product::setName);
        Optional.ofNullable(request.getSku()).ifPresent(product::setSku);
        Optional.ofNullable(request.getBarcode()).ifPresent(product::setBarcode);
        Optional.ofNullable(request.getCategoryId()).ifPresent(categoryId -> {
            Category category = categoryRepository.getById(categoryId);
            product.setCategory(category);
        });
        Optional.ofNullable(request.getWeight()).ifPresent(product::setWeight);
        Optional.ofNullable(request.getDimension()).ifPresent(product::setDimension);
        Optional.ofNullable(request.getState()).ifPresent(product::setState);
        Optional.ofNullable(request.getStatus()).ifPresent(product::setStatus);

        // Attributes
        // TODO haukc: optimize performance
        Optional.ofNullable(request.getAttributes()).ifPresent(attributeRequests -> {
            attributeRepository.deleteAllByProductId(product.getId());

            List<Attribute> attributes = new ArrayList<>();
            for (ProductRequest.AttributeRequest attributeRequest : attributeRequests) {
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

        // Options
        // TODO haukc: optimize performance
        Optional.ofNullable(request.getOptions()).ifPresent(optionRequests -> {
            optionRepository.deleteAllByProductId(product.getId());
            product.getOptions().clear();

            List<OptionModel> optionModels = new ArrayList<>();
            for (ProductRequest.OptionRequest optionRequest : optionRequests) {
                OptionModel optionModel = OptionModel.builder()
                        .name(optionRequest.getName())
                        .product(product).build();
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
        variantRepository.deleteAllByProductId(product.getId());
        product.getVariants().clear();
        this.genVariants(product, product.getOptions());
        productRepository.save(product);

        Optional.ofNullable(request.getActive()).ifPresent(product::setIsActive);
        product.setGeneralManager(gm);

        return productRepository.save(product);
    }

    private void genVariants(Product product, List<OptionModel> options) {
        List<OptionValue> combination = Arrays.asList(new OptionValue[options.size()]);
        int i = 0, j = 0;
        this.genVariants(product, options, combination, i, j);
    }

    private void genVariants(Product product, List<OptionModel> options, List<OptionValue> combination, int i, int j) {
        if (i == options.size()) {
            Variant variant = Variant.builder()
                    .stock(1L)
                    .costPrice(new BigDecimal(1000))
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
            return;
        }

        for (OptionValue optionValue : options.get(i).getOptionValues()) {
            combination.set(j, optionValue);
            genVariants(product, options, combination, i + 1, j + 1);
        }
    }
}
