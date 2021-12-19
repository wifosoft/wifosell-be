package com.wifosell.zeus.repository;

import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.exception.AppException;
import com.wifosell.zeus.model.shop.Shop;
import com.wifosell.zeus.payload.GApiErrorBody;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {
  /* Remove when don't use the jointable.
   @Query("SELECT COUNT(u.id) FROM User u INNER JOIN u.shops s WHERE s.id = (:shop_id) AND u.id = (:user_id)")
    Long existsShopByUser(@Param("user_id") Long userId,
                                   @Param("shop_id") Long shopId);

    @Query("SELECT CASE WHEN COUNT(u.id) > 0 THEN true ELSE false END FROM User u INNER JOIN u.shops s WHERE s.id = (:shop_id) AND u.id = (:user_id)")
    boolean existsUserMangageShop(@Param("user_id") Long userId,
                          @Param("shop_id") Long shopId);*/

    default Shop getShopById(Long shopId){
        return findById(shopId).orElseThrow( () -> new AppException(GApiErrorBody.makeErrorBody(EAppExceptionCode.SHOP_NOT_FOUND)));
    }
}
