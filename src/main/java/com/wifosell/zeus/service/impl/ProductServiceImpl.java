package com.wifosell.zeus.service.impl;

import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.exception.AppException;
import com.wifosell.zeus.model.category.Category;
import com.wifosell.zeus.model.product.Product;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.payload.GApiErrorBody;
import com.wifosell.zeus.payload.request.product.ProductRequest;
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
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, UserRepository userRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Product> getAllRootProducts() {
        return productRepository.findAllRootProducts();
    }

    @Override
    public List<Product> getRootProductsByUserId(Long userId) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        return productRepository.findRootProductsByGeneralManagerId(gm.getId());
    }

    @Override
    public List<Product> getRootProductsByShopIdAndSaleChannelId(Long shopId, Long saleChannelId) {
        return productRepository.findRootProductsByShopIdAndSaleChannelId(shopId, saleChannelId);
    }

    @Override
    public List<Product> getRootProductsByShopId(Long shopId) {
        return productRepository.findRootProductsByShopId(shopId);
    }

    @Override
    public List<Product> getRootProductsBySaleChannelId(Long saleChannelId) {
        return productRepository.findRootProductsBySaleChannelId(saleChannelId);
    }

    @Override
    public List<Product> getProductsByParentProductId(Long parentProductId) {
        return productRepository.findProductsByParentProductId(parentProductId);
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

    @Override
    public Product activateProduct(Long productId) {
        Product product = productRepository.findProductById(productId, true);
        product.setIsActive(true);
        return productRepository.save(product);
    }

    @Override
    public Product deactivateProduct(Long productId) {
        Product product = productRepository.findProductById(productId);
        product.setIsActive(false);
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
        Optional.ofNullable(productRequest.getStock()).ifPresent(product::setStock);
        Optional.ofNullable(productRequest.getParentId()).ifPresent(parentProductId -> {
            Product parentProduct = productRepository.findById(parentProductId).orElseThrow(
                    () -> new AppException(GApiErrorBody.makeErrorBody(EAppExceptionCode.PARENT_PRODUCT_NOT_FOUND))
            );
            product.setParent(parentProduct);
        });
    }
}
