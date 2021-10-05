package com.wifosell.zeus.repository;

import com.wifosell.zeus.model.role.UserRoleRelation;
import com.wifosell.zeus.model.shop.UserShopRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserShopRelationRepository extends JpaRepository<UserShopRelation,  Long>{
    @Query("SELECT CASE WHEN COUNT(u.id) > 0 THEN true ELSE false END FROM UserShopRelation u WHERE  u.shop = (:shop_id) AND u.user = (:user_id)")
    boolean existsUserShopRelationsByUserIsAndShopIs(@Param("user_id") Long userId,
                                                    @Param("shop_id") Long shopId);
}
