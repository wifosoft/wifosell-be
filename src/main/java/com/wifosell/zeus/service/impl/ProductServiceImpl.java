package com.wifosell.zeus.service.impl;

import com.wifosell.zeus.model.category.Category;
import com.wifosell.zeus.model.product.Attribute;
import com.wifosell.zeus.model.product.Product;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.payload.request.product.ProductRequest;
import com.wifosell.zeus.repository.AttributeRepository;
import com.wifosell.zeus.repository.CategoryRepository;
import com.wifosell.zeus.repository.ProductRepository;
import com.wifosell.zeus.repository.UserRepository;
import com.wifosell.zeus.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@Service("Product")
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final AttributeRepository attributeRepository;
    private final UserRepository userRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository,
                              CategoryRepository categoryRepository,
                              AttributeRepository attributeRepository,
                              UserRepository userRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.attributeRepository = attributeRepository;
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
        Optional.ofNullable(productRequest.getAttributes()).ifPresent(attributes -> {
            attributeRepository.deleteAttributesByProductId(product.getId());
            for (Attribute attribute : attributes) {
                attribute.setProduct(product);
                attributeRepository.save(attribute);
            }
            product.setAttributes(attributes);
        });
    }
}
