package com.wifosell.zeus.service.impl;

import com.wifosell.zeus.model.category.Category;
import com.wifosell.zeus.model.option.Option;
import com.wifosell.zeus.model.product.Attribute;
import com.wifosell.zeus.model.product.OptionProductRelation;
import com.wifosell.zeus.model.product.Product;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.payload.request.product.ProductRequest;
import com.wifosell.zeus.repository.*;
import com.wifosell.zeus.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional
@Service("Product")
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final AttributeRepository attributeRepository;
    private final OptionRepository optionRepository;
    private final OptionProductRelationRepository optionProductRelationRepository;
    private final UserRepository userRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository,
                              CategoryRepository categoryRepository,
                              AttributeRepository attributeRepository,
                              OptionRepository optionRepository,
                              OptionProductRelationRepository optionProductRelationRepository,
                              UserRepository userRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.attributeRepository = attributeRepository;
        this.optionRepository = optionRepository;
        this.optionProductRelationRepository = optionProductRelationRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByUserId(Long userId) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        return productRepository.findProductsByGeneralManagerId(gm.getId());
    }

    @Override
    public Product getProduct(Long productId) {
        return productRepository.findProductById(productId);
    }

    @Override
    public Product addProduct(Long userId, ProductRequest productRequest) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        Product product = new Product();
        this.updateProductByRequest(product, productRequest);
        product.setGeneralManager(gm);
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Long productId, ProductRequest productRequest) {
        Product product = productRepository.findProductById(productId);
        this.updateProductByRequest(product, productRequest);
        return productRepository.save(product);
    }

    private void updateProductByRequest(Product product, ProductRequest productRequest) {
        Optional.ofNullable(productRequest.getName()).ifPresent(product::setName);
        Optional.ofNullable(productRequest.getSku()).ifPresent(product::setSku);
        Optional.ofNullable(productRequest.getBarcode()).ifPresent(product::setBarcode);
        Optional.ofNullable(productRequest.getCategoryId()).ifPresent(categoryId -> {
            Category category = categoryRepository.findCategoryById(categoryId);
            product.setCategory(category);
        });
        Optional.ofNullable(productRequest.getWeight()).ifPresent(product::setWeight);
        Optional.ofNullable(productRequest.getDimension()).ifPresent(product::setDimension);
        Optional.ofNullable(productRequest.getState()).ifPresent(product::setState);
        Optional.ofNullable(productRequest.getStatus()).ifPresent(product::setStatus);

        // Attributes
        // TODO haukc: optimize performance
        Optional.ofNullable(productRequest.getAttributeRequests()).ifPresent(attributeRequests -> {
            attributeRepository.deleteAttributesByProductId(product.getId());

            List<Attribute> attributes = new ArrayList<>();
            for (ProductRequest.AttributeRequest attributeRequest : attributeRequests) {
                Attribute attribute = Attribute.builder()
                        .name(attributeRequest.getName())
                        .value(attributeRequest.getValue())
                        .product(product)
                        .build();
                attributes.add(attributeRepository.save(attribute));
            }
            product.setAttributes(attributes);
            productRepository.save(product);
        });

        // Options
        // TODO haukc: optimize performance
        Optional.ofNullable(productRequest.getOptionRequests()).ifPresent(optionRequests -> {
            optionProductRelationRepository.deleteProductOptionRelationByProductId(product.getId());

            List<OptionProductRelation> relations = new ArrayList<>();
            for (ProductRequest.OptionRequest optionRequest : optionRequests) {
                Option option = optionRepository.findOptionById(optionRequest.getId());
                OptionProductRelation relation = OptionProductRelation.builder()
                        .product(product)
                        .option(option)
                        .build();
                relations.add(optionProductRelationRepository.save(relation));
            }
            product.setOptionProductRelations(relations);
            productRepository.save(product);
        });

        Optional.ofNullable(productRequest.getActive()).ifPresent(product::setIsActive);
    }
}
