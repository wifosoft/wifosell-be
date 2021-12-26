package com.wifosell.zeus.repository;

import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.exception.AppException;
import com.wifosell.zeus.model.product.Product;
import com.wifosell.zeus.model.sale_channel.SaleChannel;
import com.wifosell.zeus.payload.GApiErrorBody;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends SoftDeleteCrudRepository<Product, Long> {
    @Transactional
    @Query("select p from Product p where p.isActive = true and p.generalManager.id = ?1")
    List<Product> findProductsByGeneralManagerId(Long generalManagerId);

    default Product findProductById(Long productId) {
        return this.findProductById(productId, false);
    }

    default Product findProductById(Long productId, boolean includeInactive) {
        Optional<Product> optionalProduct = includeInactive ? this.findById(productId) : this.findOne(productId);
        return optionalProduct.orElseThrow(
                () -> new AppException(GApiErrorBody.makeErrorBody(EAppExceptionCode.PRODUCT_NOT_FOUND))
        );
    }
}
