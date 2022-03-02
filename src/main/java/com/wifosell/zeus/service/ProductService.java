package com.wifosell.zeus.service;

import com.wifosell.zeus.model.product.Product;
import com.wifosell.zeus.payload.request.product.ProductRequest;

import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();
    List<Product> getProductsByUserId(Long userId);
    Product getProduct(Long productId);
    Product addProduct(Long userId, ProductRequest productRequest);
    Product updateProduct(Long productId, ProductRequest productRequest);
}
