package com.wifosell.zeus.repository;

import com.wifosell.framework.repository.GMSoftRepository;
import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.model.order.OrderModel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface OrderRepository extends GMSoftRepository<OrderModel, Long> {
    @Override
    default EAppExceptionCode getExceptionCodeEntityNotFound() {
        return EAppExceptionCode.ORDER_NOT_FOUND;
    }

    @Transactional
    @Query("select o from OrderModel o " +
            "where o.shop.id = :shopId " +
            "and o.generalManager.id = :gmId")
    List<OrderModel> findAllByShopIdWithGm(
            @Param("gmId") Long gmId,
            @Param("shopId") Long shopId
    );

    @Transactional
    @Query("select o from OrderModel o " +
            "where o.shop.id = :shopId " +
            "and o.generalManager.id = :gmId " +
            "and o.isActive = :isActive")
    List<OrderModel> findAllByShopIdWithGmAndActive(
            @Param("gmId") Long gmId,
            @Param("shopId") Long shopId,
            @Param("isActive") boolean isActive
    );

    @Transactional
    @Query("select o from OrderModel o " +
            "where o.saleChannel.id = :saleChannelId " +
            "and o.generalManager.id = :gmId")
    List<OrderModel> findAllBySaleChannelIdWithGm(
            @Param("gmId") Long gmId,
            @Param("saleChannelId") Long saleChannelId
    );

    @Transactional
    @Query("select o from OrderModel o " +
            "where o.saleChannel.id = :saleChannelId " +
            "and o.generalManager.id = :gmId " +
            "and o.isActive = :isActive")
    List<OrderModel> findAllBySaleChannelIdWithGmAndActive(
            @Param("gmId") Long gmId,
            @Param("saleChannelId") Long saleChannelId,
            @Param("isActive") boolean isActive
    );

    @Transactional
    @Query("select o from OrderModel o " +
            "where o.shop.id = :shopId " +
            "and o.saleChannel.id = :saleChannelId " +
            "and o.generalManager.id = :gmId")
    List<OrderModel> findAllByShopIdAndSaleChannelIdWithGm(
            @Param("gmId") Long gmId,
            @Param("shopId") Long shopId,
            @Param("saleChannelId") Long saleChannelId
    );

    @Transactional
    @Query("select o from OrderModel o " +
            "where o.shop.id = :shopId " +
            "and o.saleChannel.id = :saleChannelId " +
            "and o.generalManager.id = :gmId " +
            "and o.isActive = :isActive")
    List<OrderModel> findAllByShopIdAndSaleChannelIdWithGmAndActive(
            @Param("gmId") Long gmId,
            @Param("shopId") Long shopId,
            @Param("saleChannelId") Long saleChannelId,
            @Param("isActive") boolean isActive
    );
}
