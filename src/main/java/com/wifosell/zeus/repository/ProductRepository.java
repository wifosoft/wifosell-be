package com.wifosell.zeus.repository;

import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.exception.AppException;
import com.wifosell.zeus.model.product.Product;
import com.wifosell.zeus.payload.GApiErrorBody;
import lombok.NonNull;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import springfox.documentation.annotations.ApiIgnore;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@ApiIgnore
@Repository
public interface ProductRepository extends SoftDeleteCrudRepository<Product, Long> {
    @Transactional
    @NonNull
    @Query("select p from Product p where p.isActive = true and p.parent is null ")
    List<Product> findAllRootProducts();

    @Transactional
    @Query("select p from Product p where p.isActive = true and p.parent is null and p.generalManager.id = ?1")
    List<Product> findRootProductsByGeneralManagerId(Long generalManagerId);

    @Transactional
    @Query("select p from Product p join ProductSaleChannelShopRelation r on p.id = r.product.id where p.isActive = true and p.parent is null and r.shop.id = ?1 and r.saleChannel.id = ?2")
    List<Product> findRootProductsByShopIdAndSaleChannelId(Long shopId, Long saleChannelId);

    @Transactional
    @Query("select p from Product p join ProductSaleChannelShopRelation r on p.id = r.product.id where p.isActive = true and p.parent is null and r.shop.id = ?1")
    List<Product> findRootProductsByShopId(Long shopId);

    @Transactional
    @Query("select p from Product p join ProductSaleChannelShopRelation r on p.id = r.product.id where p.isActive = true and p.parent is null and r.saleChannel.id = ?1")
    List<Product> findRootProductsBySaleChannelId(Long saleChannelId);

    @Transactional
    @Query("select p from Product p where p.isActive = true and p.parent.id = ?1")
    List<Product> findProductsByParentProductId(Long parentProductId);

    @Transactional
    default Product findProductById(Long productId) {
        return this.findProductById(productId, false);
    }

    @Transactional
    default Product findProductById(Long productId, boolean includeInactive) {
        Optional<Product> optionalProduct = includeInactive ? this.findById(productId) : this.findOne(productId);
        return optionalProduct.orElseThrow(
                () -> new AppException(GApiErrorBody.makeErrorBody(EAppExceptionCode.PRODUCT_NOT_FOUND))
        );
    }
}
