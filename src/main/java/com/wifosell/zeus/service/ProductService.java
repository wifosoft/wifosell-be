package com.wifosell.zeus.service;

import com.wifosell.zeus.model.product.Product;
import com.wifosell.zeus.payload.request.product.AddProductRequest;
import com.wifosell.zeus.payload.request.product.UpdateProductRequest;
import lombok.NonNull;
import org.springframework.data.domain.Page;

import javax.validation.Valid;
import java.util.List;

public interface ProductService {
    Page<Product> getProducts(
            Long userId,
            List<Long> warehouseIds,
            Integer minQuantity,
            Integer maxQuantity,
            List<Boolean> isActives,
            Integer offset,
            Integer limit,
            String sortBy,
            String orderBy
    );

    List<Product> searchProducts(
            Long userId,
            String keyword,
            List<Long> warehouseIds,
            Integer minQuantity,
            Integer maxQuantity,
            List<Boolean> isActives,
            Integer offset,
            Integer limit
    );

    Product getProduct(Long userId, @NonNull Long productId);

    Product addProduct(@NonNull Long userId, @Valid AddProductRequest request);

    Product updateProduct(@NonNull Long userId, @NonNull Long productId, @Valid UpdateProductRequest request);

    Product activateProduct(Long userId, @NonNull Long productId);

    Product deactivateProduct(Long userId, @NonNull Long productId);

    List<Product> activateProducts(Long userId, @NonNull List<Long> productIds);

    List<Product> deactivateProducts(Long userId, @NonNull List<Long> productIds);
}
