package com.wifosell.zeus.service;

import com.wifosell.zeus.model.product.Product;
import com.wifosell.zeus.payload.request.product.ProductRequest;

import java.util.List;

public interface ProductService {
    List<Product> getAllRootProducts();
    List<Product> getRootProductsByUserId(Long userId);
    List<Product> getRootProductsByShopIdAndSaleChannelId(Long shopId, Long saleChannelId);
    List<Product> getRootProductsByShopId(Long shopId);
    List<Product> getRootProductsBySaleChannelId(Long saleChannelId);
    List<Product> getProductsByParentProductId(Long parentProductId);
    Product getProduct(Long productId);
    Product addProduct(Long userId, ProductRequest productRequest);
    Product updateProduct(Long productId, ProductRequest productRequest);
    Product activateProduct(Long productId);
    Product deactivateProduct(Long productId);
}
