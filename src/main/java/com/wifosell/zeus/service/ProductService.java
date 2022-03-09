package com.wifosell.zeus.service;

import com.wifosell.zeus.model.product.Product;
import com.wifosell.zeus.payload.request.product.AddProductRequest;
import com.wifosell.zeus.payload.request.product.UpdateProductRequest;
import lombok.NonNull;

import javax.validation.Valid;
import java.util.List;

public interface ProductService {
    List<Product> getAllProducts(Boolean isActive);

    List<Product> getProducts(@NonNull Long userId, Boolean isActive);

    Product getProduct(@NonNull Long userId, @NonNull Long productId);

    Product addProduct(@NonNull Long userId, @Valid AddProductRequest request);

    Product updateProduct(@NonNull Long userId, @NonNull Long productId, @Valid UpdateProductRequest request);

    Product activateProduct(@NonNull Long userId, @NonNull Long productId);

    Product deactivateProduct(@NonNull Long userId, @NonNull Long productId);

    List<Product> activateProducts(@NonNull Long userId, @NonNull List<Long> productIds);

    List<Product> deactivateProducts(@NonNull Long userId, @NonNull List<Long> productIds);
}
